package run.freshr.common.data;

import lombok.Builder;
import lombok.Data;

/**
 * Search 모델
 *
 * @author FreshR
 * @apiNote Search 데이터가 가공되어 S 클래스에 작성될 데이터
 * @since 2024. 3. 26. 오후 5:24:57
 */
@Data
@Builder
public class SearchData {

  /**
   * 대상 필드 이름
   *
   * @apiNote 대상 필드 이름<br>
   *          필드 이름과 대상 필드 이름을 분리한 이유는 Generic 유형에 대한 고려<br>
   *          필드 유형이 'ID' 라면 Long, String, Integer 세 개의 필드를 생성
   * @since 2024. 3. 26. 오후 5:24:57
   */
  private String name;
  /**
   * 대상 필드 유형
   *
   * @apiNote 대상 필드의 데이터 타입
   * @since 2024. 3. 26. 오후 5:24:57
   */
  private String type;
  /**
   * 대상 필드의 서브 유형
   *
   * @apiNote List 유형에 대해 Generic 데이터 타입을 작성<br>
   *          하지만 다른 유형이나 뎁스가 더 늘어나는 항목에 대한 고려가 안되어 있음
   * @since 2024. 3. 26. 오후 5:24:57
   */
  private String subType;
  /**
   * 데이터 포맷
   *
   * @apiNote DateTimeFormat 이 있는 경우 해당 패턴 값을 읽어서 저장
   * @since 2024. 3. 26. 오후 5:24:57
   */
  private String format;
  /**
   * 설명
   *
   * @apiNote 필드에 대한 설명
   * @since 2024. 3. 26. 오후 5:24:57
   */
  private String comment;

}
