# Library > Docs > Search Docs
> VO 객체의 설명을 작성하기 위한 Module  
> RestDocs 기능을 사용하여 테스트 코드를 작성할 때 QueryParameter 의 내용은 Entity 를 기반으로 작성하지 않는 경우가 많이 있다.
> 그럼 테스트 코드에서 각 parameter 에 대한 설명을 수동으로 작성할 수 밖에 없다.  
> 수동으로 작성하게 되면 오타나 작업자마다 다른 단어나 설명으로 작성할 수 있다.  
> 이 부분을 통일하고자 parameter 에 대한 설명을 설정할 수 있는 Module 이다.
> - ## [SearchClass](./src/main/java/run/freshr/common/annotations/SearchClass.java)
>> VO class 에 작성하는 Annotation  
>> - base: 공통 필드를 정의한 공통(또는 추상) class 여부
>>   여러 VO 에서 사용할 공통 parameter 를 정의한 class 에 true 로 설정
>>   기본 값은 false
>> - extend: 공통 필드를 정의한 공통(또는 추상) class 상속 여부
>>   공통 parameter 를 정의한 class 를 상속받은 VO class 에 true 로 설정
>>   기본 값은 true
>> 
> - ## [SearchComment](./src/main/java/run/freshr/common/annotations/SearchComment.java)
>> VO class field 에 작성하는 Annotation  
>> parameter 설명을 작성하는 Annotation 이다.
>
> - ## [SearchDocsData](./src/main/java/run/freshr/common/data/SearchDocsData.java)
>> Annotation 에 설정한 내용을 가공하기 쉽도록 처리할 수 있는 모델
>
> - ## [SearchData](./src/main/java/run/freshr/common/data/SearchData.java)
>> S 클래스의 Data 모델  
>> Q 클래스의 필드가 Path 로 구성되어 있는 것처럼 S 클래스의 필드는 해당 클래스의 구조로 이루어져 있다.
>
> - ## [SearchDocsProcessor](./src/main/java/run/freshr/common/processor/SearchDocsProcessor.java)
>> 컴파일 시점에 설정한 Annotation 내용으로 테스트 코드에서 사용하기 편한 Class 를 생성  
>> A 라는 VO class 에 @SearchClass 를 사용하면 SA 라는 class 가 생성된다.  
>> prefix `S` 는 search 의 앞 글자를 따서 작성
> 
> - ## Description
>> SearchDocs 란 RestDocs 를 작성하는데 수동으로 작성해야할 수 밖에 없는 설명 부분을 보완하기 위해 만들었다.  
>> Swagger 를 사용하면 해당 기능은 불필요할 수도 있다.  
>> 하지만 RestDocs 를 사용하거나, RestDocs & Swagger 의 형태로 사용하는 경우 테스트 코드 작성시  
>> 수동으로 설명을 입력하게 되면 오타나 통일되지 않은 내용과 같은 문제에 마주칠 수 있고,  
>> 설명을 수정해야하는 상황을 마주쳤다면 수정 범위가 넓다는 문제가 있다.  
>> 이런 부분을 해결하고자 QueryDsl 이 Compile 시점에 Entity 들을 QEntity 로 변환 생성하는 것에 아이디어를 얻어  
>> VO class 들을 Compile 시점에 가공하기 쉽도록 변환 생성하는 Module 을 제작하게 되었다.
> 
