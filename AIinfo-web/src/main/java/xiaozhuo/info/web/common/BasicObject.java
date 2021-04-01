package xiaozhuo.info.web.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author chenzhuo
 * @date   2021-02-06 16:33
 */
@Data
@NoArgsConstructor
public class BasicObject implements Serializable {
    @JsonIgnore
    private Long createId;

    @JsonIgnore
    private Long updateId;

    @JsonIgnore
    private LocalDateTime createTime;

    @JsonIgnore
    private LocalDateTime updateTime;

    @JsonIgnore
    private Integer delFlag;
}