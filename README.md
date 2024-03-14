# Project - Delivery

---

# 👥 팀원소개

🤴 이승배 

👷 김진욱, 김도현, 현민영

## 🥃 Github 주소

 🏛️ [Project - Delivery](https://github.com/deliveryChallenge/deliveryChallenge)

## 🥃 팀 노션 주소

[Project - Delivery](https://www.notion.so/Project-Delivery-7b5a4966a2a647b6b7fba0e056ab026b?pvs=21) 

# 🎯 프로젝트 주제, 목표

주제 : 가이드라인 / 음식 배달 서비스

목표 : 실전 프로젝트에 들어가기 앞서 챌린지프로젝트에 필요한 기술들을 도입해서 성능 개선 (검색, 조회) 

# 🎞️ 프로젝트 시연

[Delivery Challenge - 시연영상.mp4](https://file.notion.so/f/f/cdd52087-ff95-45e9-bb13-4280bf230c63/6ca55683-8e77-48e8-bc9e-55b705c6b579/Delivery_Challenge_-_%EC%8B%9C%EC%97%B0%EC%98%81%EC%83%81.mp4?id=6fded6e9-0150-461b-9b87-6b9239fa570b&table=block&spaceId=cdd52087-ff95-45e9-bb13-4280bf230c63&expirationTimestamp=1710504000000&signature=6gwynCLwnDUa0IYnj_D2PACxtmVmpErFWDcGkkA6Bpg&downloadName=Delivery+Challenge+-+%EC%8B%9C%EC%97%B0%EC%98%81%EC%83%81.mp4)

---

# 💫 트러블 슈팅 및 기술적 의사 결정 👇

![image](https://github.com/deliveryChallenge/deliveryChallenge/assets/140101271/32968d72-0882-4474-976a-04b93dcba82f)

위치 : [팀 노션](https://www.notion.so/Project-Delivery-7b5a4966a2a647b6b7fba0e056ab026b?pvs=21) 내 중간부분 작업 정리 (트러블 슈팅)

## 🔑 spring security - refresh token

- spring security를 이용하여 회원가입, 로그인을 구현하였고, refresh 토큰을 로컬 스토리지에 담는 방식으로 구현.

![image](https://github.com/deliveryChallenge/deliveryChallenge/assets/140101271/c4aeb8bd-01fd-41c2-8994-4f2a7e318ea2)


- 트러블 슈팅
    - 응답에는 토큰이 잘 들어오지만 로컬 스토리지에는 null값이 담기는 문제
        - 원인 분석 : login.html에 헤더에 있는 값을 전달하고 storage에 저장하기 전에 리다이렉트 되서 문제가 생김.
        - 해결 방법 : 로그인 성공 핸들러에 있는 리다이렉트 부분의 코드를 삭제
    
    ```jsx
    @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException {
            String email = extractUsername(authentication);
            String accessToken = jwtService.createAccessToken(email);
            String refreshToken = jwtService.createRefreshToken();
    
            jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    
            memberRepository.findByEmail(email)
                    .ifPresent(user -> {
                        user.updateRefreshToken(refreshToken);
                        memberRepository.saveAndFlush(user);
                    });
    
            // 로그인 성공 후 index.html로 리다이렉트 부분을 삭제
            super.setDefaultTargetUrl("http://localhost:8080/restaurants");
            super.onAuthenticationSuccess(request, response, authentication);
        }
    ```
    

## 👷 HTTPS / CI-CD + 무중단배포

- HTTPS 적용
1. 가비아 (https://www.gabia.com/) 에서 도메인 구매
2. **AWS Certificate Manager 도메인 인증서 발급** 
3. AWS Route53 호스팅 영역 생성
4. AWS ALB 생성 → https 포트 리스너 규칙 설정

- **CI-CD + 무중단배포**

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/cdd52087-ff95-45e9-bb13-4280bf230c63/bdaed072-651f-4123-ace4-8d5a6603f43c/Untitled.png)

<aside>
💡 **Github Actions, Docker, Nginx, AWS EC2**를 이용하여 
블루/그린 방식으로 무중단배포 CI-CD 파이프라인 구성

</aside>

- **기술적 의사결정**

**Github Actions :** 

기술적 의사결정 과정에서 고려되었던 기술 중 CI-CD 쪽에서 비교되었던 두 아키텍처는 Github Actions와 Jenkins이다. 두가지 후보군을 두고 여러 자료들을 참고해본 결과 크게는 아래와 같이 정리할 수 있었다.

|  | Github Actions | Jenkins |
| --- | --- | --- |
| 레퍼런스 | 비교적 적음 | 많음 |
| 난이도 | 간단함 | 비교적 어려움 |
| 처리속도 | 비교적 느림 | 비교적 빠름 |

또한, 대규모 시스템엔 Jenkins가 더 적합하고 소규모 프로젝트에는 Github Action이 괜찮다는 자료가 많아 실전프로젝트 기간 들어가기 전 사전주차격인 지금에는 규모도 작고, ec2 인스턴스도 한개만 사용할거라 본 프로젝트에서는 Github Actions을 써보기로 결정했다.

**Docker :**

S3와 CodeDeploy 조합 대신 Docker를 선택한 이유는 다음과 같다. 
 Docker 컨테이너는 이식성이 뛰어나고 Docker 지원을 통해 모든 시스템에서 일관되게 실행될 수 있어 S3 및 CodeDeploy를 사용한 기존 배포에 비해 플랫폼 독립성이 뛰어나다. 
 또한, CodeDeploy는 AWS 환경과의 연동에 대한 이해와 학습곡선이 존재하고, 환경별로 관리해줘야한다는 단점이 있다하여 이번 2주동안 진행하는 프로젝트에서는 Docker를 사용하였다.

**Nginx :**

EC2 프리티어로 서버를 운영할 계획이기 때문에 nginx의 “리버스 프록시” 기능을 통해 한대의 서버를 이용해 배포가 가능했고, 구현하기 쉽고 추가 비용이 없었기에 사용하였다.

- 트러블 슈팅
    - github-actions.yml에서 deploy to develop 부분에서 `docker/io timeout` 오류
        - **원인 추론** : EC2 SSH 소스유형이 내IP로 되어있어 GitHub Actions 의 IP가 접근 불가한 상태
        - **해결 방법** : EC2 보안그룹에 인바인드 규칙으로 내 IP만 허용이 아닌 모든 IP(0.0.0.0/0) 허용으로 변경
    - 배포 작업을 기다리는 중에서 무한 대기 중 문제
        - **원인추론** : github actions runner 가 실행 종료되었기 때문.
        - **해결방안** : github actions runner를 백그라운드에서도 계속 실행중으로 바꾸기 위해서 ec2에서 actions-runner 경로로 옮겨 nohub 명령어로 재실행
            
            ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/cdd52087-ff95-45e9-bb13-4280bf230c63/5b9a2a84-cb5f-4206-babd-0d28328200b8/Untitled.png)
            
    - 아직 해결하지 못한 Trouble
    **하나의 인스턴스에 블루그린 무중단배포 후, 50% 확률로 502 Bad Gateway …**
        - **원인 추론 -** 로드밸런서에 8080, 8081 포트 리스너의 호스팅을 https 443포트로 리다이렉팅 시킨 후, 인스턴스로 향하는 대상그룹으로 트래픽을 전달해주고 있다. 
         8080, 8081포트 두개가 번갈아가며 꺼지고, 켜지기 때문에 443 포트에 대상그룹 두개를 지정해두면 가중치가 절반이 되어 50% 확률로 502 Bad Gateway가 나온다.
        - **임시 해결** : 8080포트(blue) 컨테이너가 실행중일때는 로드밸런서에서 443포트 대상그룹전달을 8080 대상그룹을 통해 인스턴스로 호스팅을 100% 전달하여 502 Bad Gateway를 없앰. 8081포트일경우 똑같이 적용.
        
        ![스크린샷 2024-01-25 오후 4.23.19.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/cdd52087-ff95-45e9-bb13-4280bf230c63/56dce5e6-6e72-4f31-abd4-3795cee8e23b/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-01-25_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_4.23.19.png)
        

## 🔎 elasticsearch, queryDSL, JPA 쿼리메서드 각각 기능구현과 비교테스트

![엘라스틱 텟트.PNG](https://prod-files-secure.s3.us-west-2.amazonaws.com/cdd52087-ff95-45e9-bb13-4280bf230c63/a81a93ac-357d-4572-a77d-bc014451e521/%EC%97%98%EB%9D%BC%EC%8A%A4%ED%8B%B1_%ED%85%9F%ED%8A%B8.png)

![엘라스티 결과.PNG](https://prod-files-secure.s3.us-west-2.amazonaws.com/cdd52087-ff95-45e9-bb13-4280bf230c63/15337468-c9c5-401f-9165-e8c0c3f8107f/%EC%97%98%EB%9D%BC%EC%8A%A4%ED%8B%B0_%EA%B2%B0%EA%B3%BC.png)

- 사용 소감
    - JPA 메소드 사용 시
        1. 유연한 검색이 가능하나 불편(조건 검색)
        2. 조건 검색을 적용하려면 수 많은 조건문으로 직접 하드코딩을 해야될 것으로 예상
    - QueryDSL 사용 시
        1. 유연한 검색이 가능하다.
        2. QueryDSL 라이브러리를 통해 쉽고 간편하게 조건 검색 구현이 가능
    - Elasticsearch 사용 시
        1. 역인덱싱으로 빠른 검색속도를 기대했으나 속도에 차이가 없음
        2. 로컬에서만 사용했음에도 불구하고 설정이 매우 귀찮음, 학습곡선이 매우 큰것같다.
        3. 풀텍스트검색이 필요할 땐 극적인 성능향상이 될거같지만, 현재의 프로젝트에선 의미가 없는것 같고 오히려 무거운 프로그램에 ELK 스택까지 같이 사용이 권장되 더욱 부담됨

- 트러블 슈팅
    - 어떻게 검색을 하든 인덱스의 모든 문서들을 가져오는 문제
        - 원인 추론 : 찾지 못함
        - 해결 방법 : criteria 생성 부분과 쿼리 전송 부분을 분리
    
    ```jsx
    //이전 코드
    @RequiredArgsConstructor
    @Component
    public class RestaurantSearchRepositoryImpl{
    
        private final ElasticsearchOperations elasticsearchOperations;
    
        public Page<RestaurantDocument> searchByCriteria(String restaurantName, String address, String category, Pageable pageable) {
            Criteria criteria = new Criteria();
    
            if (!restaurantName.isEmpty()) {
                criteria.and("restaurant_name").contains(restaurantName);
            }
            if (!address.isEmpty()) {
                criteria.and("address").contains(address);
            }
            if (!category.isEmpty()) {
                criteria.and("category").contains(category);
            }
    
            Query query = new CriteriaQuery(criteria).setPageable(pageable);
            SearchHits<RestaurantDocument> search = elasticsearchOperations.search(query, RestaurantDocument.class);
    
            List<RestaurantDocument> content = new ArrayList<>();
            for (SearchHit<RestaurantDocument> hit : search) {
                content.add(hit.getContent());
            }
    
            return new PageImpl<>(content, pageable, search.getTotalHits());
        }
    }
    ```
    
    ```jsx
    //변경 코드
    @RequiredArgsConstructor
    @Component
    public class RestaurantSearchRepositoryImpl{
    
        private final ElasticsearchOperations elasticsearchOperations;
    
        public Page<RestaurantDocument> searchByCriteria(String restaurantName, String address, String category, Pageable pageable) {
    
            Criteria criteria = buildCriteria(restaurantName, address, category);
            CriteriaQuery query = new CriteriaQuery(criteria).setPageable(pageable);
    
            SearchHits<RestaurantDocument> search = elasticsearchOperations.search(query, RestaurantDocument.class);
    
            List<RestaurantDocument> content = new ArrayList<>();
            for (SearchHit<RestaurantDocument> hit : search) {
                content.add(hit.getContent());
            }
            return new PageImpl<>(content, pageable, search.getTotalHits());
        }
        private Criteria buildCriteria(String restaurantName, String address, String category) {
            Criteria criteria = new Criteria();
    
            if (!StringUtils.isEmpty(restaurantName)) {
                criteria = criteria.and("restaurant_name").contains(restaurantName);
            }
            if (!StringUtils.isEmpty(address)) {
                criteria = criteria.and("address").contains(address);
            }
            if (!StringUtils.isEmpty(category)) {
                criteria = criteria.and("category").is(category);
            }
    
            return criteria;
        }
    }
    ```
    

- 페이징 처리를 위해 Pageable을 넘기게 되면 all shards fail
    - 원인 추론 : 분류 기준인 sort의 값이 넘어가면 해당 에러 발생하는 것 확인. 이유는 찾지 못함
    - 해결 방법 : Default 값으로 페이지 넘버, 사이즈만 넘겨주는 것으로 임시 해결

```jsx
@GetMapping("/testES2")
    public ResponseEntity searchCriteria(
            @RequestParam(required = false) String restaurantName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String category,
            @PageableDefault(size = 20, page = 1) Pageable pageable) {//sort값을 넘기지 않는것으로 임시해결
        Page<RestaurantDocument> restaurantDocumentPage = esService.searchCriteria(restaurantName, address, category, pageable);
        List<RestaurantDocument> restaurantDocumentList = restaurantDocumentPage.getContent();

        PageDto pageDto = new PageDto<>(ResDocResponseDto.fromDocsListEntity(restaurantDocumentList), restaurantDocumentPage);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
```

## 🧰 redis cache를 이용한 조회 기능 개선

- cache 방식을 이용하면 조회 쪽에서 성능 개선할 수 있을 거라 생각하여 도입

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/cdd52087-ff95-45e9-bb13-4280bf230c63/6ec125c3-8e00-4aea-a735-78e96abfd4a7/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/cdd52087-ff95-45e9-bb13-4280bf230c63/af34eb12-8518-40d2-98a9-439db3175b07/Untitled.png)

위 사진과 같은 조건으로 테스트를 했을때,
오류 0%, 평균 조회속도 6120MS의 수치를 얻을 수 있었음.

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/cdd52087-ff95-45e9-bb13-4280bf230c63/1f776766-e07c-48e1-930b-84d0ba5259bc/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/cdd52087-ff95-45e9-bb13-4280bf230c63/05f15fc3-a4fa-4319-b227-157d0ea6411d/Untitled.png)

3천명의 트래픽을 조건으로 설정 하였을때 평균 13775MS, 7%의 오류를 보였다.

응답시간의 해결을 위해 아래와 같이 Redis를 도입해 보았다.

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/cdd52087-ff95-45e9-bb13-4280bf230c63/a2474a4b-9191-4434-a31b-31a596ccb5d3/Untitled.png)

그리고, 테스트의 성능 개선 결과를 확인하기 위해 세팅은 동일하게 하였다.

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/cdd52087-ff95-45e9-bb13-4280bf230c63/f8541ca2-2d73-434c-a310-5738958bdd38/Untitled.png)

이후 나온 결과

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/cdd52087-ff95-45e9-bb13-4280bf230c63/6d1292e4-a32b-4a18-81ea-6da6e9b47b9b/Untitled.png)

12000개의 표본 처리동안, 평균 2368MS의 속도를 보였고, 0%의 오류율을 보였다.

즉, Redis의 단순한 적용만으로 82.71%의 성능 개선을 확인 할 수 있었다.
