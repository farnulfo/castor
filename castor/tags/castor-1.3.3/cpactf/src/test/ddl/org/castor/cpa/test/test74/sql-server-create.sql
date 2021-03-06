create table test74_group_person (
  gid int         not null,
  pid int        not null 
)
go

create index test74_group_person_p_pk on test74_group_person ( pid )
go

create index test74_group_person_g_pk on test74_group_person ( gid )
go

create table test74_many_group (
  gid       int           not null,
  value1    varchar(100)  not null
)
go

create unique index test74_many_group_pk on test74_many_group ( gid )
go


create table test74_many_person (
   pid      int          not null,
   value1   varchar(100) not null,
   helloworld varchar(100) null,
   sthelse varchar(100) null
)
go

create unique index test74_many_person_pk on test74_many_person ( pid )
go
