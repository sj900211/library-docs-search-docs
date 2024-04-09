package run.freshr.common.processor;

import static java.util.Objects.isNull;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.tools.Diagnostic.Kind.NOTE;

import com.google.auto.service.AutoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.javapoet.ClassName;
import org.springframework.javapoet.FieldSpec;
import org.springframework.javapoet.JavaFile;
import org.springframework.javapoet.TypeSpec;
import run.freshr.common.annotations.SearchClass;
import run.freshr.common.annotations.SearchComment;
import run.freshr.common.data.SearchData;
import run.freshr.common.data.SearchDocsData;
import run.freshr.common.data.SearchDocsData.SearchDocsDataBuilder;

/**
 * Process 정의
 *
 * @author FreshR
 * @apiNote Compile 에서 동작할 Process 정의
 * @since 2024. 3. 27. 오전 9:45:47
 */
@Slf4j
@AutoService(Processor.class)
public class SearchDocsProcessor extends AbstractProcessor {

  /**
   * 지원 Annotation 유형 설정
   *
   * @return the supported annotation types
   * @apiNote 지원 Annotation 유형 설정
   * @author FreshR
   * @since 2024. 3. 27. 오전 9:45:47
   */
  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of(SearchComment.class.getName());
  }

  /**
   * 지원 소스 버전 설정
   *
   * @return the supported source version
   * @apiNote 지원 소스 버전 설정
   * @author FreshR
   * @since 2024. 3. 27. 오전 9:45:47
   */
  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  /**
   * Process
   *
   * @param annotations the annotations
   * @param roundEnv    the round env
   * @return the boolean
   * @apiNote Compile 에서 동작할 로직 정의
   * @author FreshR
   * @since 2024. 3. 27. 오전 9:45:47
   */
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    try {
      if (annotations.isEmpty()) {
        return false;
      }

      processingEnv.getMessager().printMessage(NOTE, "SearchCommentProcessor.process");

      for (Element element : roundEnv.getRootElements()) {
        if (element.getKind() != CLASS) {
          continue;
        }

        SearchClass searchClass = element.getAnnotation(SearchClass.class);

        if (isNull(searchClass)) {
          continue;
        }

        List<SearchDocsData> fieldList = new ArrayList<>();
        TypeElement typeElement = (TypeElement) element;

        if (searchClass.extend()) {
          TypeMirror superMirror = typeElement.getSuperclass();

          if (!isNull(superMirror)) {
            DeclaredType superDeclared = (DeclaredType) superMirror;
            Element superElement = superDeclared.asElement();
            TypeElement superTypeElement = (TypeElement) superElement;

            fieldList.addAll(getFieldList(superTypeElement));
          }
        }

        fieldList.addAll(getFieldList(typeElement));
        List<SearchDocsData> distinctFieldList = fieldList.stream()
            .filter(distinctByKey(SearchDocsData::getField)).toList();

        /*
         * 필드 스펙 정의
         */
        List<FieldSpec> fieldSpecList = distinctFieldList.stream()
            .map(item -> FieldSpec.builder(SearchData.class, item.getField())
                .addModifiers(PUBLIC, STATIC, FINAL)
                .initializer(
                    "SearchData.builder()"
                        + ".name(\"" + item.getName() + "\")"
                        + ".type(\"" + item.getType() + "\")"
                        + (item.getIsList() ? ".subType(\"" + item.getSubType() + "\")" : "")
                        + ".format(\"" + item.getFormat() + "\")"
                        + ".comment(\"" + item.getComment() + "\")"
                        + ".build();"
                ).build()
            ).toList();

        ClassName className = ClassName.get(typeElement);
        TypeSpec typeSpec = TypeSpec
            .classBuilder("S" + className.simpleName())
            .addModifiers(PUBLIC)
            .addFields(fieldSpecList)
            .build();

        // 클래스 생성
        JavaFile
            .builder(className.packageName(), typeSpec)
            .build()
            .writeTo(processingEnv.getFiler());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return true;
  }

  /**
   * 필드 목록 조회
   *
   * @param typeElement the type element
   * @return the field list
   * @apiNote 필드 목록을 {@link SearchDocsData} 로 만들어서 반환
   * @author FreshR
   * @since 2024. 3. 27. 오전 9:45:47
   */
  public List<SearchDocsData> getFieldList(TypeElement typeElement) {
    // 멤버 요소 목록 조회
    List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
    List<SearchDocsData> commentList = new ArrayList<>();

    for (Element field : enclosedElements) {
      // 필드 유형인지 체크
      if (field.getKind() != FIELD) {
        continue;
      }

      String fieldName = field.getSimpleName().toString();
      SearchComment searchComment = field.getAnnotation(SearchComment.class);
      DateTimeFormat dateTimeFormat = field.getAnnotation(DateTimeFormat.class);

      String comment = "";
      String format = "";

      if (!isNull(searchComment)) {
        comment = searchComment.value();
      }

      if (!isNull(dateTimeFormat)) {
        format = dateTimeFormat.pattern();
      }

      String type = field.asType().toString();
      String listType = List.class.getName();
      boolean isList = type.contains(listType);
      String subType = "";

      if (isList) {
        subType = type.substring(type.indexOf("<") + 1, type.lastIndexOf(">"));
      }

      // Generic ID 유형의 경우 분기 처리
      if (type.equals("ID")) {
        SearchDocsDataBuilder docsBuilder = SearchDocsData.builder()
            .name(fieldName)
            .comment(comment)
            .format(format)
            .subType(subType)
            .isList(false);

        commentList.add(docsBuilder.field(fieldName + "Long").type("java.lang.Long").build());
        commentList.add(docsBuilder.field(fieldName + "Integer").type("java.lang.Integer").build());
        commentList.add(docsBuilder.field(fieldName + "String").type("java.lang.String").build());
      } else {
        commentList.add(SearchDocsData
            .builder()
            .field(fieldName)
            .name(fieldName)
            .comment(comment)
            .format(format)
            .type(type)
            .subType(subType)
            .isList(isList)
            .build());
      }
    }

    return commentList;
  }

  /**
   * 중복 필드를 제거
   *
   * @param <T>          the type parameter
   * @param keyExtractor the key extractor
   * @return the predicate
   * @apiNote 중복 필드를 제거하기 위한 조건 정의
   * @author FreshR
   * @since 2024. 3. 27. 오전 9:45:47
   */
  public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
    Map<Object, Boolean> map = new ConcurrentHashMap<>();

    return item -> map.putIfAbsent(keyExtractor.apply(item), Boolean.TRUE) == null;
  }

}
