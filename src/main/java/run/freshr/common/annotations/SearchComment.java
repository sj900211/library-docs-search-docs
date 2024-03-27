package run.freshr.common.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Get Parameter VO 필드에 적용할 Annotation
 *
 * @author FreshR
 * @apiNote Get Parameter VO 필드에 적용할 Annotation
 * @since 2024. 3. 26. 오후 5:24:57
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface SearchComment {

  /**
   * 설명
   *
   * @return the string
   * @apiNote 필드에 대한 설명
   * @author FreshR
   * @since 2024. 3. 26. 오후 5:24:57
   */
  String value() default "";

}
