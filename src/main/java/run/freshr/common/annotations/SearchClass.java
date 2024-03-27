package run.freshr.common.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Get Parameter VO 클래스에 적용할 Annotation
 *
 * @author FreshR
 * @apiNote Get Parameter VO 클래스에 적용할 Annotation
 * @since 2024. 3. 26. 오후 5:24:36
 */
@Target(TYPE)
@Retention(SOURCE)
public @interface SearchClass {

  /**
   * 공통 필드를 정의한 공통(또는 추상) 클래스 여부
   *
   * @return the boolean
   * @apiNote 공통 필드를 정의한 공통(또는 추상) 클래스 여부<br>
   *          공통 필드를 정의한 클래스가 아니라면 따로 작성하지 않아도 됨
   * @author FreshR
   * @since 2024. 3. 26. 오후 5:24:36
   */
  boolean base() default false;

  /**
   * 공통 필드를 정의한 공통(또는 추상) 클래스 상속 여부
   *
   * @return the boolean
   * @apiNote 공통 필드를 정의한 공통(또는 추상) 클래스 상속 여부<br>
   *          공통 필드를 정의한 클래스를 상속받은 것이 아니라면 따로 작성하지 않아도 됨
   * @author FreshR
   * @since 2024. 3. 26. 오후 5:24:36
   */
  boolean extend() default true;

}
