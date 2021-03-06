CREATE TABLE test31_group (
  id      numeric(10,0)  not null,
  value1  varchar(200)   not null
)
GO
CREATE UNIQUE INDEX test31_group_pk ON test31_group ( id )
GO

CREATE TABLE test31_persistent (
  id        integer          not null,
  ctime     datetime         not null,
  mtime     datetime         null,
  value1    varchar(200)     not null,
  parent_id integer          null,
  group_id  numeric(10,0)    not null
)
GO
CREATE UNIQUE INDEX test31_persistent_pk ON test31_persistent ( id )
GO

CREATE TABLE test31_related (
  id          integer     not null,
  persist_id  integer     not null
)
GO
CREATE UNIQUE INDEX test31_related_pk ON test31_related ( id )
GO

CREATE TABLE test31_extends1 (
  ident   integer         not null,
  ext     integer         not null
)
GO
CREATE UNIQUE INDEX test31_extends1_pk ON test31_extends1 ( ident )
GO

CREATE TABLE test31_extends2 (
  id      integer         not null,
  ext     integer         not null
)
GO
CREATE UNIQUE INDEX test31_extends2_pk ON test31_extends2 ( id )
GO

CREATE TABLE test31_relation (
  id1   integer         not null,
  id2   integer         not null
)
GO
CREATE UNIQUE INDEX test31_relation_pk ON test31_relation ( id1, id2 )
GO
