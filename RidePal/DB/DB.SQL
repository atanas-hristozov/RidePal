create table artists
(
    id               bigint                                  not null
        primary key,
    artist_name      varchar(255) collate utf8mb4_unicode_ci null,
    artist_photo     varchar(255)                            null,
    artist_tracklist varchar(255)                            null
)
    charset = latin1;

create table genres
(
    id         int          not null
        primary key,
    genre_name varchar(255) not null
)
    charset = latin1;

create table albums
(
    id            bigint auto_increment
        primary key,
    title         varchar(255) null,
    tracklist_url varchar(255) null,
    genre_id      int          not null,
    constraint albums_genres_id_fk
        foreign key (genre_id) references genres (id)
);

create table playlists
(
    id            int auto_increment
        primary key,
    title         varchar(255) not null,
    `rank`        double       not null,
    playlist_time int          not null
);

create table playlist_genres
(
    playlist_id int not null,
    genre_id    int not null,
    constraint playlist_genres_genres_id_fk
        foreign key (genre_id) references genres (id)
            on update cascade on delete cascade,
    constraint playlist_genres_playlists_id_fk
        foreign key (playlist_id) references playlists (id)
            on update cascade on delete cascade
);

create table tracks
(
    id          bigint       not null
        primary key,
    title       varchar(255) null,
    duration    int          not null,
    `rank`      double       not null,
    preview_url varchar(255) null,
    artist_id   bigint       not null,
    album_id    bigint       null,
    genre_id    int          not null,
    constraint tracks_albums_id_fk
        foreign key (album_id) references albums (id),
    constraint tracks_artists_id_fk
        foreign key (artist_id) references artists (id)
            on update cascade on delete cascade,
    constraint tracks_genres_id_fk
        foreign key (genre_id) references genres (id)
);

create table users
(
    id         int auto_increment
        primary key,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    username   varchar(255) not null,
    password   varchar(255) not null,
    email      varchar(255) not null,
    is_admin   tinyint(1)   not null,
    user_photo varchar(255) null
);

create table playlist_data
(
    playlist_id int    not null,
    user_id     int    not null,
    track_id    bigint not null,
    constraint playlist_data_playlists_id_fk
        foreign key (playlist_id) references playlists (id)
            on update cascade on delete cascade,
    constraint playlist_data_tracks_id_fk
        foreign key (track_id) references tracks (id)
            on update cascade on delete cascade,
    constraint playlist_data_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete cascade
);

