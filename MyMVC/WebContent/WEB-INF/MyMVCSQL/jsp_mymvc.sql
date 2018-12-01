show user;

-- >> jsp & Servlet (MyMVC)
-- [181105]

select *
from jsp_member
order by idx desc;

-- 1. MVC패턴으로 한 줄 메모장 만들기
-- #테이블 생성
create table jsp_memo
(idx         number(8)     not null        -- 글번호(시퀀스로 입력)
,fk_userid   varchar2(20)  not null        -- 회원아이디
,name        varchar2(20)  not null        -- 작성자이름
,msg         varchar2(100)                 -- 메모내용
,writedate   date default sysdate          -- 작성일자
,cip         varchar2(20)                  -- 클라이언트 IP 주소
,status      number(1) default 1 not null  -- 글삭제유무
,constraint  PK_jsp_memo_idx primary key(idx)
,constraint  FK_jsp_memo_userid foreign key(fk_userid)
                                  references jsp_member(userid)
,constraint  CK_jsp_memo_status check(status in(0,1) )  
);

-- #시퀀스 생성(메모번호)
create sequence jsp_memo_idx
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

String sql = "select idx, fk_userid, name, msg\n"+
"        , to_char(writedate, 'yyyy-mm-dd hh24:mi:ss') as writedate\n"+
"        , cip\n"+
"from jsp_memo\n"+
"where status = 1\n"+
"order by idx desc";


select *
from jsp_memo
order by idx desc;

-- [181107]
select *
from jsp_member
order by idx desc;

update jsp_member set coin=50000000, point=30000
where userid ='leess';

commit;


-- [181108]
insert into jsp_member(idx, userid, name, pwd, email, HP1, HP2, HP3)
values(seq_jsp_member.nextval, 'admin', '관리자', '9695b88a59a1610320897fa84cb7e144cc51f2984520efb77111d94b402a8382', 'j2LiNyRA76BOstW3lQeu6Wax626gcHY8Hmqi9TjccVU', '010', 'N+pZjKf5DA7KkI0SjOj9ww==','mxRR0DkoS5JSAKOeT/wWzg==');

commit;

delete from jsp_member where userid='admin';




String sql = "select idx, fk_userid, name, msg\n"+
"        , to_char(writedate, 'yyyy-mm-dd hh24:mi:ss') as writedate\n"+
"        , cip\n"+
"from jsp_memo\n"+
"where status = 1\n"+
"order by idx desc";


select rno, idx, fk_userid, name, msg,  writedate, cip
from
    (
    select rownum as rno, idx, fk_userid, name, msg,  writedate, cip
    from
        (
        select idx, fk_userid, name, msg,  to_char(writedate, 'yyyy-mm-dd hh24:mi:ss') as writedate, cip
        from jsp_memo
        where status =1
        order by idx desc
        ) V
    )T
where T.rno between 1 and 10;



String sql = "select rno, idx, fk_userid, name, msg,  writedate, cip\n"+
"from\n"+
"    (\n"+
"    select rownum as rno, idx, fk_userid, name, msg,  writedate, cip\n"+
"    from\n"+
"        (\n"+
"        select idx, fk_userid, name, msg,  to_char(writedate, 'yyyy-mm-dd hh24:mi:ss') as writedate, cip\n"+
"        from jsp_memo\n"+
"        where status =1\n"+
"        order by idx desc\n"+
"        ) V\n"+
"    )T\n"+
"where T.rno between ? and ?";

select *
from jsp_memo
where name like '홍길동';


-- [181109] 
-- 한줄메모장 삭제한 글을 백업하는 테이블
create table jsp_memo_delete
(idx         number(8)     not null                 -- 글번호(시퀀스로 입력)
,userid   varchar2(20)  not null              -- 회원아이디
,name        varchar2(20)  not null             -- 작성자이름
,msg         varchar2(100)                           -- 메모내용
,writedate   date                                       -- 작성일자
,cip         varchar2(20)                               -- 클라이언트 IP 주소
,status      number(1)  not null                    -- 글삭제유무
,deletedate date default sysdate  not null    -- 글삭제한 시간
,constraint  PK_jsp_memo_delete_idx primary key(idx)
);

-- #시퀀스 생성(메모번호)
create sequence jsp_memo_delete_idx
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

select *
from jsp_memo_delete;

drop sequence jsp_memo_delete_idx;

-- 휴면계정 전환; 마지막 로그인 일시, 마지막 비밀번호 변경 날짜 컬럼 추가
select *
from jsp_member;

alter table jsp_member
add  lastlogindate date default sysdate;

alter table jsp_member
add lastpwdchangedate date default sysdate;

update jsp_member set lastpwdchangedate = add_months(lastpwdchangedate, -7)
where userid='leess';

commit;

select *
from jsp_member
where userid='leess';


select idx, userid, name, coin, point
from jsp_member
where status=1 and
lastpwdchangedate > add_months(sysdate, -6) and 
userid='leess' ;

-- [181112]
select idx, userid, name, coin, point
        , trunc( months_between(sysdate, lastpwdchangedate) ) as pwdchangegap
        , trunc( months_between(add_months(sysdate, -6), lastlogindate) ) as lastlogingap
from jsp_member
order by idx asc;
-- months_between(add_months(sysdate, -6) , lastpwdchangedate)의 값이 0 이상인 경우 ==> -6개월 이후
-- months_between(add_months(sysdate, -6) , lastpwdchangedate)의 값이 0 이하인 경우 ==> -6개월 미만

String sql = "select idx, userid, name, coin, point\n"+
"        , trunc( months_between(add_months(sysdate, -6), lastpwdchangedate) ) as pwdchangegap\n"+
"        , trunc( months_between(add_months(sysdate, -6), lastlogindate) ) as lastlogingap\n"+
"from jsp_member\n"+
"order by idx asc";

-- to_yminterval('년-월') 년-월 설정값 만큼 sysdate로부터 이전 값을 출력
update jsp_member set lastlogindate = lastlogindate - to_yminterval('01-01')
where userid ='hongkd';

commit;

select idx, userid, to_char(lastlogindate, 'yyyy-mm-dd hh24:mi:ss') as lastlogindate
        , to_char(lastpwdchangedate, 'yyyy-mm-dd hh24:mi:ss') as lastpwdchangedate
        , name, coin, point
from jsp_member
where userid ='hongkd'
order by idx asc;


update jsp_member set status=1;

commit;

 ---------------------------------------------------------------------------
-- [181115]

                 ---- *** 쇼핑몰 *** ----
/*
   카테고리 테이블명 : jsp_category

   컬럼정의 
     -- 카테고리 대분류 번호  : 시퀀스(seq_jsp_category_cnum)로 증가함.(Primary Key)
     -- 카테고리 코드(unique) : ex) 전자제품  '100000'
                                  의류      '200000'
                                  도서      '300000' 
     -- 카테고리명(not null)  : 전자제품, 의류, 도서           
  
*/ 
 
create table jsp_category
(cnum    number(8)     not null  -- 카테고리 대분류 번호
,code    varchar2(20)  not null  -- 카테고리 코드
,cname   varchar2(100) not null  -- 카테고리명
,constraint PK_jsp_category_cnum primary key(cnum)
,constraint UQ_jsp_category_code unique(code)
);

create sequence seq_jsp_category_cnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into jsp_category values(seq_jsp_category_cnum.nextval, '100000', '전자제품');
insert into jsp_category values(seq_jsp_category_cnum.nextval, '200000', '의류');
insert into jsp_category values(seq_jsp_category_cnum.nextval, '300000', '도서');
insert into jsp_category values(seq_jsp_category_cnum.nextval, '400000', '식품');
commit;

select cnum, code, cname
from jsp_category;

String sql = "select cnum, code, cname\n"+
"from jsp_category";

-- spec 테이블 만들기
create table jsp_spec
(snum    number(8)     not null  -- 스펙 대분류 번호
,sname   varchar2(100) not null  -- 스펙명
,constraint PK_jsp_spec_snum primary key(snum)
,constraint UQ_jsp_spec_sname unique(sname)
);

create sequence seq_jsp_spec_snum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


insert into jsp_spec values(seq_jsp_spec_snum.nextval, 'HIT');
insert into jsp_spec values(seq_jsp_spec_snum.nextval, 'NEW');
insert into jsp_spec values(seq_jsp_spec_snum.nextval, 'BEST');

commit;


select snum, sname
from jsp_spec;

String sql = "select snum, sname\n"+
"from jsp_spec";



---- *** 제품 테이블 : jsp_product *** ----
create table jsp_product
(pnum           number(8) not null       -- 제품번호(Primary Key)
,pname          varchar2(100) not null   -- 제품명
,pcategory_fk   varchar2(20)             -- 카테고리코드(Foreign Key)
,pcompany       varchar2(50)             -- 제조회사명
,pimage1        varchar2(100) default 'noimage.png' -- 제품이미지1   이미지파일명
,pimage2        varchar2(100) default 'noimage.png' -- 제품이미지2   이미지파일명 
,pqty           number(8) default 0      -- 제품 재고량
,price          number(8) default 0      -- 제품 정가
,saleprice      number(8) default 0      -- 제품 판매가(할인해서 팔 것이므로)
,pspec          varchar2(20)             -- 'HIT', 'BEST', 'NEW' 등의 값을 가짐.
,pcontent       clob                     -- 제품설명  varchar2는 varchar2(4000) 최대값이므로
                                         --          4000 byte 를 초과하는 경우 clob 를 사용한다.
                                         --          clob 는 최대 4GB 까지 지원한다.
                                         
,point          number(8) default 0      -- 포인트 점수                                         
,pinputdate     date default sysdate     -- 제품입고일자
,constraint  PK_jsp_product_pnum primary key(pnum)
,constraint  FK_jsp_product_pcategory_fk foreign key(pcategory_fk)
                                         references jsp_category(code)
);

create sequence seq_jsp_product_pnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into jsp_product(pnum, pname, pcategory_fk, pcompany, 
                        pimage1, pimage2, pqty, price, saleprice,
                        pspec, pcontent, point)
values(seq_jsp_product_pnum.nextval, '스마트TV', '100000', '삼성',
       'tv_samsung_h450_1.png','tv_samsung_h450_2.png',
       100,1200000,800000,'HIT','42인치 스마트 TV. 기능 짱!!', 50);


insert into jsp_product(pnum, pname, pcategory_fk, pcompany, 
                        pimage1, pimage2, pqty, price, saleprice,
                        pspec, pcontent, point)
values(seq_jsp_product_pnum.nextval, '노트북', '100000', '엘지',
       'notebook_lg_gt50k_1.png','notebook_lg_gt50k_2.png',
       150,900000,750000,'HIT','노트북. 기능 짱!!', 30);  
       

insert into jsp_product(pnum, pname, pcategory_fk, pcompany, 
                        pimage1, pimage2, pqty, price, saleprice,
                        pspec, pcontent, point)
values(seq_jsp_product_pnum.nextval, '바지', '200000', 'S사',
       'cloth_canmart_1.png','cloth_canmart_2.png',
       20,12000,10000,'HIT','예뻐요!!', 5);       
       

insert into jsp_product(pnum, pname, pcategory_fk, pcompany, 
                        pimage1, pimage2, pqty, price, saleprice,
                        pspec, pcontent, point)
values(seq_jsp_product_pnum.nextval, '남방', '200000', '버카루',
       'cloth_buckaroo_1.png','cloth_buckaroo_2.png',
       50,15000,13000,'HIT','멋져요!!', 10);       
       

insert into jsp_product(pnum, pname, pcategory_fk, pcompany, 
                        pimage1, pimage2, pqty, price, saleprice,
                        pspec, pcontent, point)
values(seq_jsp_product_pnum.nextval, '세계탐험보물찾기시리즈', '300000', '아이세움',
       'book_bomul_1.png','book_bomul_2.png',
       100,35000,33000,'HIT','만화로 보는 세계여행', 20);       
       
       
insert into jsp_product(pnum, pname, pcategory_fk, pcompany, 
                        pimage1, pimage2, pqty, price, saleprice,
                        pspec, pcontent, point)
values(seq_jsp_product_pnum.nextval, '만화한국사', '300000', '녹색지팡이',
       'book_koreahistory_1.png','book_koreahistory_2.png',
       80,130000,120000,'HIT','만화로 보는 이야기 한국사 전집', 60);
       
commit;       

select * from jsp_product; 

select pnum, pname, pcategory_fk, pcompany, 
          pimage1, pimage2, pqty, price, saleprice,
          pspec, pcontent, point, to_char(pinputdate, 'yyyy-mm-dd') as pinputdate
from jsp_product
where pspec = 'HIT'
order by pnum desc;

String sql = "select pnum, pname, pcategory_fk, pcompany,  "+
"          pimage1, pimage2, pqty, price, saleprice, "+
"          pspec, pcontent, point, to_char(pinputdate, 'yyyy-mm-dd') as pinputdate "+
"from jsp_product "+
"where pspec = ? "+
"order by pnum desc";

-- [181116]
-- #제품마다 이미지파일 여러개 넣기
create table jsp_product_imagefile
(imgfileno           number               not null    -- 이미지 파일 번호; 시퀀스
,fk_pnum            number(8)            not null    -- 제품번호(fk)
,imgfilename       varchar2(100)        not null    -- 제품이미지 파일명
,constraint PK_jsp_product_imagefile primary key(imgfileno)
,constraint FK_jsp_product_imagefile foreign key(fk_pnum)
                                         references jsp_product(pnum)
);

create sequence seq_imgfileno
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

select imgfileno, fk_pnum, imgfilename
from jsp_product_imagefile;

String sql = "insert into jsp_product(pnum, pname, pcategory_fk, pcompany, \n"+
"                        pimage1, pimage2, pqty, price, saleprice,\n"+
"                        pspec, pcontent, point)\n"+
"values(seq_jsp_product_pnum.nextval, ?, ?, ?,\n"+
"       ?, ?,\n"+
"       ?, ?, ?, ?, ?, ?)";

String sql = "insert into jsp_product_imagefile(imgfileno, fk_pnum, imgfilename)\n"+
"values(seq_imgfileno.nextval, ?, ?)";


update jsp_product set pspec='HIT' where pnum = 7;

commit;



-- [181119]
select pnum, pname, pcategory_fk, pcompany, 
          pimage1, pimage2, pqty, price, saleprice,
          pspec, pcontent, point, to_char(pinputdate, 'yyyy-mm-dd') as pinputdate,
          c.cname
from jsp_product P join jsp_category C
on p.pcategory_fk = c.code
where pcategory_fk = 464646
order by pnum desc;

String sql = "select pnum, pname, pcategory_fk, pcompany, \n"+
"          pimage1, pimage2, pqty, price, saleprice,\n"+
"          pspec, pcontent, point, to_char(pinputdate, 'yyyy-mm-dd') as pinputdate,\n"+
"          c.cname\n"+
"from jsp_product P join jsp_category C\n"+
"on p.pcategory_fk = c.code\n"+
"where pcategory_fk = code\n"+
"order by pnum desc";


-------- **** 장바구니 테이블 생성하기 **** ----------

 desc jsp_member;
 desc jsp_product;

 create table jsp_cart
 (cartno  number               not null   --  장바구니 번호
 ,fk_userid  varchar2(20)         not null   --  사용자ID
 ,fk_pnum    number(8)            not null   --  제품번호 
 ,oqty    number(8) default 0  not null   --  주문량
 ,status  number(1) default 1             --  삭제유무
 ,constraint PK_jsp_cart_cartno primary key(cartno)
 ,constraint FK_jsp_cart_fk_userid foreign key(fk_userid)
                                references jsp_member(userid) 
 ,constraint FK_jsp_cart_fk_pnum foreign key(fk_pnum)
                                references jsp_product(pnum)
 ,constraint CK_jsp_cart_status check( status in(0,1) ) 
 );

 create sequence seq_jsp_cart_cartno
 start with 1
 increment by 1
 nomaxvalue
 nominvalue
 nocycle
 nocache;

 comment on table jsp_cart
 is '장바구니 테이블';

 comment on column jsp_cart.cartno
 is '장바구니번호(시퀀스명 : seq_jsp_cart_cartno)';

 comment on column jsp_cart.fk_userid
 is '회원ID  jsp_member 테이블의 userid 컬럼을 참조한다.';

 comment on column jsp_cart.fk_pnum
 is '제품번호 jsp_product 테이블의 pnum 컬럼을 참조한다.';

 comment on column jsp_cart.oqty
 is '장바구니에 담을 제품의 주문량';

 comment on column jsp_cart.status
 is '장바구니에 담겨져 있으면 1, 장바구니에서 비우면 0';

 select *
 from user_tab_comments;

 select column_name, comments
 from user_col_comments
 where table_name = 'JSP_CART';
 
 select cartno, fk_userid, fk_pnum, oqty, status
 from jsp_cart;
 
-- 181120
-- 장바구니 목록 보는 join 쿼리
select A.cartno, A.fk_userid, A.fk_pnum, A.oqty, A.status,
           B.pname, B.pcategory_fk, B.pcompany, 
           B.pimage1, B.pimage2, B.pqty, B. price, B.saleprice,
           B.pspec, B.point
 from jsp_cart A inner join jsp_product B
 on A.fk_pnum = B.pnum
 where A.status =1 and A.fk_userid = 'leess'
 order by A.cartno desc;
 
String sql = "select A.cartno, A.fk_userid, A.fk_pnum, A.oqty, A.status,\n"+
"           B.pname, B.pcategory_fk, B.pcompany, \n"+
"           B.pimage1, B.pimage2, B.pqty, B. price, B.saleprice,\n"+
"           B.pspec, B.point\n"+
"from jsp_cart A inner join jsp_product B\n"+
"on A.fk_pnum = B.pnum\n"+
"where A.status =1 and A.fk_userid = 'ssum'\n"+
"order by A.cartno desc";


-- [181121]
--- >>> 주문관련 테이블 <<< -----------------------------
-- [1] 주문 개요 테이블 : jsp_order
-- [2] 주문 상세 테이블 : jsp_order_detail


-- *** "주문 개요" 테이블 *** --
create table jsp_order
(odrcode        varchar2(20) not null          -- 주문코드(명세서번호)  주문코드 형식 : s(회사코드)+날짜+sequence ==> s20180430-1 , s20180430-2 , s20180430-3
                                               --                                                      s20180501-4 , s20180501-5 , s20180501-6
,fk_userid      varchar2(20) not null          -- 사용자ID
,odrtotalPrice  number       not null          -- 주문총액
,odrtotalPoint  number       not null          -- 주문총포인트
,odrdate        date default sysdate not null  -- 주문일자
,constraint PK_jsp_order_odrcode primary key(odrcode)
,constraint FK_jsp_order_fk_userid foreign key(fk_userid)
                                    references jsp_member(userid)
);


-- "주문코드(명세서번호) 시퀀스" 생성
create sequence seq_jsp_order
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


select odrcode, fk_userid, 
       odrtotalPrice, odrtotalPoint,
       to_char(odrdate, 'yyyy-mm-dd hh24:mi:ss') as odrdate
from jsp_order
order by odrcode desc;


-- *** "주문 상세" 테이블 *** --
create table jsp_order_detail
(odrseqnum      number               not null   -- 주문상세 일련번호
,fk_odrcode     varchar2(20)         not null   -- 주문코드(명세서번호)
,fk_pnum        number(8)            not null   -- 제품번호
,oqty           number               not null   -- 주문량
,odrprice       number               not null   -- "주문할 그때 그당시의 실제 판매가격" ==> insert 시 jsp_product 테이블에서 해당제품의 saleprice 컬럼값을 읽어다가 넣어주어야 한다.
,deliverStatus  number(1) default 1  not null   -- 배송상태( 1 : 주문만 받음,  2 : 배송시작,  3 : 배송완료)
,deliverDate    date                            -- 배송완료일자  default 는 null 로 함.
,constraint PK_jsp_order_detail_odrseqnum  primary key(odrseqnum)
,constraint FK_jsp_order_detail_fk_odrcode foreign key(fk_odrcode)
                                            references jsp_order(odrcode) on delete cascade
,constraint FK_jsp_order_detail_fk_pnum foreign key(fk_pnum)
                                         references jsp_product(pnum)
,constraint CK_jsp_order_detail check( deliverStatus in(1, 2, 3) )
);


-- "주문상세 일련번호 시퀀스" 생성
create sequence seq_jsp_order_detail
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


select odrseqnum, fk_odrcode, fk_pnum, oqty, 
       odrprice, deliverStatus, 
       to_char(deliverDate, 'yyyy-mm-dd hh24:mi:ss') as deliverDate 
from jsp_order_detail
order by odrseqnum desc;

update jsp_member set coin = 50000000 where userid ='ssum';

commit;


update jsp_order_detail set deliverStatus=1 where deliverStatus=2;
commit;
-- Multi join
select A.odrcode, A.fk_userid, to_char(A.odrdate, 'yyyy-mm-dd hh24:mi:ss') as odrdate
        , B.odrseqnum, B.fk_pnum, B.oqty, B.odrprice
        , case b.deliverstatus when 1 then '주문완료' when 2 then '배송시작' when 3 then '배송완료' end as deliverstatus
        , c.pname, c.pimage1, c.price, c.saleprice, c.point
from jsp_order A join jsp_order_detail B
on A.odrcode = B.fk_odrcode
join jsp_product C
on B.fk_pnum = C.pnum
where 1=1
and A.fk_userid = 'ssum';

String sql = "select A.odrcode, A.fk_userid, to_char(A.odrdate, 'yyyy-mm-dd hh24:mi:ss') as odrdate\n"+
"        , B.odrseqnum, B.fk_pnum, B.oqty, B.odrprice\n"+
"        , case b.deliverstatus when 1 then '주문완료' when 2 then '배송시작' when 3 then '배송완료' end as deliverstatus\n"+
"        , c.pname, c.pimage1, c.price, c.saleprice, c.point\n"+
"from jsp_order A join jsp_order_detail B\n"+
"on A.odrcode = B.fk_odrcode\n"+
"join jsp_product C\n"+
"on B.fk_pnum = C.pnum\n"+
"where 1=1\n"+
"and A.fk_userid = 'ssum'";


-- 전표(영수증번호; odrcode)로 유저의 정보 알아오기 --> 서브쿼리 사용
select *
from jsp_member
where userid = ( select fk_userid
                        from jsp_order
                        where odrcode = 's20181122-4');
                        
                        
String sql = "select *\n"+
"from jsp_member\n"+
"where userid = ( select fk_userid\n"+
"                        from jsp_order\n"+
"                        where odrcode = 's20181122-4')";


select A.odrcode, A.fk_userid, to_char(A.odrdate, 'yyyy-mm-dd hh24:mi:ss') as odrdate
        , B.odrseqnum, B.fk_pnum, B.oqty, B.odrprice, B.deliverstatus
        , c.pname, c.pimage1,  c.pimage2, c.saleprice, c.point
from jsp_order A join jsp_order_detail B
on A.odrcode = B.fk_odrcode
join jsp_product C
on B.fk_pnum = C.pnum
where (fk_odrcode||'/'||fk_pnum) in(odrcodePnum);


select A.odrcode, to_char(A.odrdate, 'yyyy-mm-dd hh24:mi:ss') as odrdate
        , B.odrseqnum, B.fk_pnum, B.oqty, B.odrprice, B.deliverstatus
        , c.pname, c.pimage1,  c.pimage2, c.saleprice, c.point
        , D.userid, D.email
from jsp_member D 
    inner join jsp_order A 
    on A.fk_userid = D.userid
    inner join jsp_order_detail B
    on A.odrcode = B.fk_odrcode
    inner join jsp_product C
    on B.fk_pnum = C.pnum
where (fk_odrcode||'/'||fk_pnum) in('s20181122-1/4');

String sql = "select A.odrcode, to_char(A.odrdate, 'yyyy-mm-dd hh24:mi:ss') as odrdate\n"+
"        , B.odrseqnum, B.fk_pnum, B.oqty, B.odrprice, B.deliverstatus\n"+
"        , c.pname, c.pimage1,  c.pimage2, c.saleprice, c.point\n"+
"        , D.userid, D.email\n"+
"from jsp_member D \n"+
"    inner join jsp_order A \n"+
"    on A.fk_userid = D.userid\n"+
"    inner join jsp_order_detail B\n"+
"    on A.odrcode = B.fk_odrcode\n"+
"    inner join jsp_product C\n"+
"    on B.fk_pnum = C.pnum\n"+
"where (fk_odrcode||'/'||fk_pnum) in(odrcodePnum)";


-- [181127]
--- Google Map API 관련 ---

create table jsp_storemap
(storeno     number  not null  -- 점포no 
,storeName   varchar2(100)     -- 점포명
,latitude    number            -- 위도
,longitude   number            -- 경도
,zindex      number            -- 우선순위(z-index) 점포no로 사용됨.
,tel         varchar2(20)      -- 전화번호
,addr        varchar2(200)     -- 주소
,transport   varchar2(1000)    -- 오시는길 
,constraint PK_jsp_storemap_storeno primary key(storeno)
);

create sequence seq_storemap
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into jsp_storemap(storeno, storeName, latitude, longitude, zindex, tel, addr, transport) 
values(seq_storemap.nextval, 'KH 종로점', 37.567957, 126.983134, seq_storemap.currval
      ,'02-1234-5678', '서울특별시 중구 남대문로 120 대일빌딩 2F, 3F', '지하철 2호선 을지로입구역 3번출구 100M / 1호선 종각역 4번, 5번 출구 200M');

insert into jsp_storemap(storeno, storeName, latitude, longitude, zindex, tel, addr, transport)  
values(seq_storemap.nextval, '롯데백화점 본점', 37.564728, 126.981641, seq_storemap.currval
      ,'02-771-2500', '서울특별시 중구 소공동 남대문로 81', '지하철 2호선 을지로입구역 8번출구');

insert into jsp_storemap(storeno, storeName, latitude, longitude, zindex, tel, addr, transport)  
values(seq_storemap.nextval, '신세계백화점 본점', 37.560227, 126.980773, seq_storemap.currval
      ,'1588-1234', '서울특별시 중구 명동 소공로 63', '지하철 4호선 회현역 7번출구');

commit;

select storeno, storeName, latitude, longitude, zindex, tel, addr, transport 
from jsp_storemap
order by storeno;

String sql = "select storeno, storeName, latitude, longitude, zindex, tel, addr, transport \n"+
"from jsp_storemap\n"+
"order by storeno";


create table jsp_storedetailImg
(seq         number not null    -- 일련번호
,fk_storeno  number not null    -- 점포no
,img         varchar2(500)      -- 매장이미지
,constraint PK_jsp_storedetailImg_seq primary key(seq)
,constraint FK_jsp_storedetailImg foreign key(fk_storeno)
                                  references jsp_storemap(storeno)
                                  on delete cascade
);

create sequence seq_storedetailImg
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into jsp_storedetailImg(seq, fk_storeno, img)
values(seq_storedetailImg.nextval, 1, 'kh01.png');

insert into jsp_storedetailImg(seq, fk_storeno, img)
values(seq_storedetailImg.nextval, 1, 'kh02.png');

insert into jsp_storedetailImg(seq, fk_storeno, img)
values(seq_storedetailImg.nextval, 1, 'kh03.png');

insert into jsp_storedetailImg(seq, fk_storeno, img)
values(seq_storedetailImg.nextval, 2, 'lotte01.png');

insert into jsp_storedetailImg(seq, fk_storeno, img)
values(seq_storedetailImg.nextval, 2, 'lotte02.png');

insert into jsp_storedetailImg(seq, fk_storeno, img)
values(seq_storedetailImg.nextval, 3, 'newworld01.png');
 
commit; 
 

select storeno, storeName, tel, addr, transport  
from jsp_storemap 
where storeno = 1;

select seq, fk_storeno, img
from jsp_storedetailImg;

select img
from jsp_storedetailImg
where fk_storeno = 1;

select storeno, storeName, latitude, longitude, zindex, tel, addr, transport, img 
from jsp_storemap a join jsp_storedetailImg b
on storeno = fk_storeno
where storeno = 1;



-- [181129]
--------------- *** Ajax Study *** --------------------
create table tbl_ajaxnews
(seqtitleno   number not null
,title        varchar2(200) not null
,registerday  date default sysdate not null
,constraint PK_tbl_ajaxnews_seqtitleno primary key(seqtitleno)
);

create sequence seq_tbl_ajaxnews_seqtitleno
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into tbl_ajaxnews(seqtitleno, title) values(seq_tbl_ajaxnews_seqtitleno.nextval, '''남달라'' 박성현 LPGA 투어 텍사스 클래식 우승, 시즌 첫 승' );
insert into tbl_ajaxnews(seqtitleno, title) values(seq_tbl_ajaxnews_seqtitleno.nextval, '뼈아픈 연패-전패, 아직 한번도 못 이겼다고?' );
insert into tbl_ajaxnews(seqtitleno, title) values(seq_tbl_ajaxnews_seqtitleno.nextval, '전설들과 어깨 나란히 한 김해림 "4연패도 노려봐야죠"');
insert into tbl_ajaxnews(seqtitleno, title) values(seq_tbl_ajaxnews_seqtitleno.nextval, '삼성·현대차 들쑤신 엘리엇, 이번엔 伊 최대통신사 삼켰다');
insert into tbl_ajaxnews(seqtitleno, title) values(seq_tbl_ajaxnews_seqtitleno.nextval, '"야구장, 어떤 음악으로 채우나" 응원단장들도 괴롭다');
insert into tbl_ajaxnews(seqtitleno, title) values(seq_tbl_ajaxnews_seqtitleno.nextval, '"공부 후 10분의 휴식, 기억력 높인다"');
insert into tbl_ajaxnews(seqtitleno, title) values(seq_tbl_ajaxnews_seqtitleno.nextval, '현대차, 쏘나타 ''익스트림 셀렉션'' 트림 출시… 사양과 가격은?');
insert into tbl_ajaxnews(seqtitleno, title) values(seq_tbl_ajaxnews_seqtitleno.nextval, '날씨무더위 계속…곳곳 강한 소나기');

commit;

select *
from tbl_ajaxnews;

update tbl_ajaxnews set registerday = registerday - 1
where seqtitleno in (1,2);

commit;

select seqtitleno
     , case when length(title) > 22 then substr(title, 1, 20)||'..'
       else title end as title
     , to_char(registerday, 'yyyy-mm-dd') as registerday
from tbl_ajaxnews
where to_char(registerday, 'yyyy-mm-dd') = to_char(sysdate, 'yyyy-mm-dd');


insert into tbl_ajaxnews(seqtitleno, title) values(seq_tbl_ajaxnews_seqtitleno.nextval, '서울에 첫 폭염경보…수도권과 영서도 경보로 강화');
commit;

insert into tbl_ajaxnews(seqtitleno, title) values(seq_tbl_ajaxnews_seqtitleno.nextval, '코스피, 외국인·기관 동반 매도에 1,990선 ''털썩''');
commit;


insert into tbl_ajaxnews(seqtitleno, title) values(seq_tbl_ajaxnews_seqtitleno.nextval, '테스트하는 신문기사글');
commit;

insert into tbl_ajaxnews(seqtitleno, title) values(seq_tbl_ajaxnews_seqtitleno.nextval, '테스트하는 신문기사글2');
commit;


create table tbl_images
(userid varchar2(20) not null
,name   varchar2(20) not null
,img    varchar2(50) not null
,constraint PK_tbl_images_userid primary key(userid)
);

insert into tbl_images(userid, name, img) values('iyou','아이유','iyou.jpg');
insert into tbl_images(userid, name, img) values('kimth','김태희','kimth.jpg');
insert into tbl_images(userid, name, img) values('parkby','박보영','parkby.jpg');

commit;

select *
from tbl_images;

select *
from jsp_member
where name like '%'||''||'%';

-- [181130]
create table tbl_books
(subject        varchar2(200) not null
,title          varchar2(200) not null
,author         varchar2(200) not null
,registerday    date default sysdate
);

insert into tbl_books(subject, title, author) values('소설','해질무렵','황석영');
insert into tbl_books(subject, title, author) values('소설','마션','앤디위어');
insert into tbl_books(subject, title, author) values('소설','어린왕자','생텍쥐페리');
insert into tbl_books(subject, title, author) values('소설','당신','박범신');
insert into tbl_books(subject, title, author) values('소설','삼국지','이문열');
insert into tbl_books(subject, title, author) values('프로그래밍','ORACLE','이순신');
insert into tbl_books(subject, title, author) values('프로그래밍','자바','안중근');
insert into tbl_books(subject, title, author) values('프로그래밍','JSP Servlet','똘똘이');
insert into tbl_books(subject, title, author) values('프로그래밍','스프링','윤봉길');

commit;

select subject, title, author, registerday
from tbl_books;