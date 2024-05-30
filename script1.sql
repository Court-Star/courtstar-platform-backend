create table account
(
    account_id int auto_increment
        primary key,
    idelete    bit          not null,
    email      varchar(80)  null,
    first_name varchar(30)  null,
    last_name  varchar(30)  null,
    password   varchar(255) null,
    phone      varchar(10)  null
);

create table invalidated_token
(
    id          varchar(255) not null
        primary key,
    expiry_time datetime(6)  null
);

create table permission
(
    name        varchar(255) not null
        primary key,
    description varchar(255) null
);

create table role
(
    name        varchar(255) not null
        primary key,
    description varchar(255) null
);

create table account_roles
(
    account_account_id int          not null,
    roles_name         varchar(255) not null,
    primary key (account_account_id, roles_name),
    constraint FKi0yba44vmeofgedvm64b10ogv
        foreign key (roles_name) references role (name),
    constraint FKo8y2pfsrp39xin2e90bfpo4bb
        foreign key (account_account_id) references account (account_id)
);

create table role_permissions
(
    role_name        varchar(255) not null,
    permissions_name varchar(255) not null,
    primary key (role_name, permissions_name),
    constraint FKcppvu8fk24eqqn6q4hws7ajux
        foreign key (role_name) references role (name),
    constraint FKf5aljih4mxtdgalvr7xvngfn1
        foreign key (permissions_name) references permission (name)
);


