package xiaozhuo.info.web.common.vo;

import lombok.Data;
import xiaozhuo.info.persist.base.Idea;

import java.util.List;

/**
 * @author chenzhuo
 * @date   2021-04-01
 */
@Data
public class IdeaVO {

    private String time;

    private List<Idea> ideaList;

}
