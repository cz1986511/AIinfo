package xiaozhuo.info.persist.base;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author chenzhuo
 * @date   2021-04-01
 */
@Data
public class Idea {
    private Long id;

    private String time;

    private String content;

    private LocalDate gmtCreate;

    private LocalDate gmtModify;

}