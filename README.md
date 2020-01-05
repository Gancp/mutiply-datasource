
# mutiply-datasource
mybatis的多数据源配置，以及支持JTA全局事务



1。运行的时候先创建2个数据库和2个数据表
数据库 ：test
表：
create table mtds_test.demo0
(
    id   int                       not null,
    mark varchar(128) charset utf8 null,
    constraint demo_id_uindex
        unique (id)
)
    comment 'demo0';

alter table mtds_test.demo0
    add primary key (id);
    

数据库：test2
create table mtds_test2.demo2
(
    id   int                       not null,
    mark varchar(128) charset utf8 null,
    constraint demo_id_uindex
        unique (id)
)
    comment 'demo';

alter table mtds_test2.demo2
    add primary key (id);    


2.实体包，dao包和mapperXMl文件的包一定要按照数据源的名称区分

鸣谢:参考了这位作者的思路,https://github.com/louislivi/fastdep

修改: 修改为使用自动注册bean, 不需要繁琐的手工编写bean
