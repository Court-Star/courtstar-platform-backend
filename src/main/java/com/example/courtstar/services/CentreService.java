package com.example.courtstar.services;

import com.example.courtstar.dto.request.CentreRequest;
import com.example.courtstar.dto.request.CourtRequest;
import com.example.courtstar.dto.response.CentreNameResponse;
import com.example.courtstar.dto.response.CentreResponse;
import com.example.courtstar.dto.response.CourtResponse;
import com.example.courtstar.entity.*;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
import com.example.courtstar.mapper.CentreMapper;
import com.example.courtstar.mapper.CourtMapper;
import com.example.courtstar.repositories.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity

public class CentreService {
    @Autowired
    CentreRepository centreRepository;
    @Autowired
    CentreMapper centreMapper;
    @Autowired
    AccountReponsitory accountReponsitory;
    @Autowired
    CourtRepository courtRepository;
    @Autowired
    CourtMapper courtMapper;
    @Autowired
    private CentreManagerRepository centreManagerRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private ImgRepository imgRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public CentreResponse createCentre(CentreRequest request) {
        Centre centre = centreMapper.toCentre(request);
        return centreMapper.toCentreResponse(centreRepository.save(centre));
    }

    public List<CentreResponse> getAllCentres() {
        return centreRepository.findAll().stream().map(centreMapper::toCentreResponse).toList();
    }

    public List<CentreResponse> getAllCentresIsActive(boolean isActive) {
        return centreRepository.findAllByIsDeleteAndStatus(false, isActive).stream().map(centreMapper::toCentreResponse).toList();
    }

    public CentreResponse getCentre(int id) {
        return centreMapper.toCentreResponse(centreRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_CENTRE)));
    }

    public Set<CentreNameResponse> getAllCentresOfManager(String email){
        Account account = accountReponsitory.findByEmail(email).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        CentreManager centreManager = centreManagerRepository.findByAccountId(account.getId()).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        Set<CentreNameResponse> centreResponses = centreManager.getCentres().stream()
                .filter(centre -> !centre.isDelete())
                .map(centre -> {
                    CentreNameResponse centreResponse = CentreNameResponse.builder()
                            .id(centre.getId())
                            .name(centre.getName())
                            .build();
                    return centreResponse;
                }).collect(Collectors.toSet());
        return centreResponses;
    }

    public Boolean isActive(int id, boolean active) {
        Centre centre = centreRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_CENTRE));
        centre.setStatus(active);
        centreRepository.save(centre);
        return true;
    }

    public Boolean delete(int id) {
        Centre centre = centreRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_CENTRE));
        centre.setDelete(true);
        centreRepository.save(centre);
        return true;
    }

    public CentreResponse addCentre(CentreRequest request){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account account = accountReponsitory.findByEmail(name).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        CentreManager manager = centreManagerRepository.findByAccountId(account.getId())
                .orElseThrow( () -> new AppException(ErrorCode.NOT_FOUND_USER));
        String role= manager.getAccount().getRole().getName();
        if(!role.equals("MANAGER")){
            throw new RuntimeException();
        }
        Centre centre = centreMapper.toCentre(request);

        List<Slot> slotList = generateSlots(centre);
        centre.setSlots(slotList);

        List<Image> imgList = generateImages(request, centre);
        centre.setImages(imgList);
        centre.setManager(manager);
        centreRepository.save(centre);

        //Fake payment method
        PaymentMethod paymentMethod = PaymentMethod.builder()
                .build();
        paymentMethodRepository.save(paymentMethod);
        centre.setPaymentMethod(paymentMethod);
        //end fake payment method

        List<Centre> centres = manager.getCentres();
        if (centres == null) {
            centres = new ArrayList<>();
            manager.setCentres(centres);
        }
        centres.add(centre);

        CourtRequest courtRequest = new CourtRequest();
        centre.setCourts(addCourt(centre.getId(),courtRequest));

        slotRepository.saveAll(slotList);
        imgRepository.saveAll(imgList);
        centreRepository.save(centre);
        centreManagerRepository.save(manager);

        CentreResponse centreResponse = centreMapper.toCentreResponse(centre);
        centreResponse.setManagerId(manager.getId());
        return centreResponse;
    }

    private List<Court> addCourt(int idCentre, CourtRequest request){
        Centre centre = centreRepository.findById(idCentre).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_CENTRE));

        List<Court> courts = new ArrayList<>();

        for(int i=0;i<centre.getNumberOfCourts();i++){
            Court court = courtMapper.toCourt(request.builder()
                    .courtNo(i+1)
                    .status(true)
                    .build());
            court.setCentre(centre);
            courts.add(courtRepository.save(court));
        }
        centre.setCourts(courts);
        return centreRepository.save(centre).getCourts();
    }

    private List<Image> generateImages(CentreRequest request, Centre centre) {
        AtomicInteger imageNo = new AtomicInteger(1);
        List<Image> imgList = request.getImages().stream().map(url -> {
            Image image = new Image();
            image.setUrl(url);
            image.setCentre(centre);
            image.setImageNo(imageNo.getAndIncrement());
            return image;
        }).collect(Collectors.toCollection(() -> new ArrayList<>()));

        return imgList;
    }

    private List<Slot> generateSlots(Centre centre) {
        List<Slot> slots = new ArrayList<>();
        int slotNo = 1;
        LocalTime currentTime = centre.getOpenTime();
        LocalTime closeTime = centre.getCloseTime();
        int slotDuration = centre.getSlotDuration();

        while (currentTime.plusHours(slotDuration).isBefore(closeTime) || currentTime.plusHours(slotDuration).equals(closeTime)) {
            Slot slot = Slot.builder()
                    .slotNo(slotNo)
                    .startTime(currentTime)
                    .endTime(currentTime.plusHours(slotDuration))
                    .centre(centre)
                    .build();
            slots.add(slot);

            currentTime = currentTime.plusHours(slotDuration);
            slotNo++;
        }

        return slots;
    }

//    public CentreResponse updateCentre(int centreId, CentreRequest request) {
//        // Lấy ngữ cảnh bảo mật và lấy tên của người dùng hiện đang xác thực
//        var context = SecurityContextHolder.getContext();
//        String name = context.getAuthentication().getName();
//
//        // Tìm tài khoản liên quan đến email của người dùng đã xác thực
//        Account account = accountReponsitory.findByEmail(name)
//                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER));
//
//        // Tìm quản lý liên quan đến tài khoản
//        CentreManager manager = centreManagerRepository.findByAccountId(account.getId())
//                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER));
//
//        // Kiểm tra xem tài khoản có vai trò "MANAGER" hay không
//        Role role = manager.getAccount().getRoles().stream()
//                .filter(i -> i.getName().equals("MANAGER"))
//                .findFirst()
//                .orElse(null);
//
//        if (role == null) {
//            throw new RuntimeException();
//        }
//
//        // Tìm trung tâm cần cập nhật
//        Centre centre = centreRepository.findById(centreId)
//                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_CENTRE));
//
//        // Cập nhật chi tiết trung tâm bằng dữ liệu từ yêu cầu
//        centre.setName(request.getName());
//        centre.setAddress(request.getAddress());
//        centre.setOpenTime(request.getOpenTime());
//        centre.setCloseTime(request.getCloseTime());
//        centre.setPricePerHour(request.getPricePerHour());
//        centre.setNumberOfCourt(request.getNumberOfCourt());
//        centre.setPaymentMethod(request.getPaymentMethod());
//        centre.setApproveDate(request.getApproveDate());
//        centre.setManager(manager);
//
//        // Quản lý danh sách hình ảnh hiện có
//        updateImages(centre, request.getImages());
//
//        // Tạo danh sách slot mới cho trung tâm
//        List<Slot> slotList = generateSlots(centre);
//        centre.getSlots().clear();
//        centre.getSlots().addAll(slotList);
//
//        // Lưu chi tiết trung tâm đã cập nhật
//        centreRepository.save(centre);
//
//        // Lưu các slot mới
//        slotRepository.saveAll(slotList);
//
//        // Tạo và lưu các sân cho trung tâm đã cập nhật
//        CourtRequest courtRequest = new CourtRequest();
//        centre.setCourts(addCourt(centre.getId(), courtRequest));
//
//        // Lưu chi tiết trung tâm và quản lý đã cập nhật
//        centreRepository.save(centre);
//        centreManagerRepository.save(manager);
//
//        // Chuyển trung tâm đã cập nhật thành đối tượng phản hồi và trả về
//        CentreResponse centreResponse = centreMapper.toCentreResponse(centre);
//        centreResponse.setManagerId(manager.getId());
//        return centreResponse;
//    }
//
//    private void updateImages(Centre centre, List<String> newImages) {
//        List<Image> currentImages = centre.getImages();
//        currentImages.clear();
//
//        AtomicInteger imageNo = new AtomicInteger(1);
//        List<Image> imgList = newImages.stream().map(url -> {
//            Image image = new Image();
//            image.setUrl(url);
//            image.setCentre(centre);
//            image.setImageNo(imageNo.getAndIncrement());
//            return image;
//        }).collect(Collectors.toCollection(() -> new ArrayList<>()));
//
//        currentImages.addAll(imgList);
//    }


}
