package xiaozhuo.info.web.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenzhuo
 * @data   2020-08-11
 */
@Data
public class ConsumeAmountVO implements Serializable {
    private static final long serialVersionUID = -7872486869955393224L;

    private String name;
    private Integer type;
    private Long amount;
}
