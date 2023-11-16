create table albums
(
    id         int auto_increment
        primary key,
    album_name varchar(255) not null
);

create table artists
(
    id          int auto_increment
        primary key,
    artist_name varchar(255) not null
);

create table geners
(
    id         int auto_increment
        primary key,
    ganer_name varchar(255) not null
);

create table tracks
(
    id          int auto_increment
        primary key,
    title       varchar(255) not null,
    duration    int          not null,
    artist_id   int          not null,
    album_id    int          not null,
    preview_url varchar(255) null,
    genre_id    int          not null,
    constraint tracks_albums_id_fk
        foreign key (album_id) references albums (id),
    constraint tracks_artists_id_fk
        foreign key (artist_id) references artists (id),
    constraint tracks_geners_id_fk
        foreign key (genre_id) references geners (id)
);

create table users
(
    id         int auto_increment
        primary key,
    username   varchar(50)  not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    password   varchar(255) not null,
    email      varchar(255) not null,
    is_admin   tinyint(1)   not null
);

create table playlists
(
    id            int auto_increment
        primary key,
    user_id       int          not null,
    title         varchar(255) not null,
    playlist_time int          not null,
    constraint playlists_users_id_fk
        foreign key (user_id) references users (id)
);

