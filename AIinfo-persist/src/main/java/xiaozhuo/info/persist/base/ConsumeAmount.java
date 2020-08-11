package xiaozhuo.info.persist.base;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author chenzhuo
 * @date   2020-08-11
 */
@Data
public class ConsumeAmount {
    private Long id;

    private String name;

    private Integer type;

    private Integer year;

    private Integer month;

    private Integer day;

    private Long amount;

    private String createrName;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModify;

}