package xiaozhuo.info.web.controller;

import com.alibaba.fastjson.TypeReference;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import xiaozhuo.info.persist.base.Idea;
import xiaozhuo.info.service.IdeaService;
import xiaozhuo.info.service.util.*;
import xiaozhuo.info.web.common.ResponseObj;
import xiaozhuo.info.web.common.ResultCode;
import xiaozhuo.info.web.common.vo.IdeaVO;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhuochen
 * @date   2021-04-01
 */
@Controller
@RequestMapping("/api/idea")
@Slf4j
@CrossOrigin
public class IdeaController {

	@Autowired
	private IdeaService ideaService;

	private static String IDEA_KEY = "ideaKey";

	@ApiOperation(value = "创建")
	@PostMapping("/create")
	@ResponseBody
	//@RequireLoginAuth
	public ResponseObj<Idea> createIdea(@RequestBody Idea idea, HttpServletRequest request){
		log.info("createIdea params:{}", idea);
		int result = ideaService.addIdea(idea);
		if (result > 0) {
			log.info("createIdea result:{}", result);
			return ResponseObj.success(result);
		} else {
			return ResponseObj.setResultCode(ResultCode.FAIL_IDEA_CREATE);
		}
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	//@RequireLoginAuth
	public ResponseObj<List<IdeaVO>> getIdeaList(@RequestParam("time")String time, HttpServletRequest request) {
		ResponseObj<List<IdeaVO>> result = new ResponseObj<>();
		if (null == time) {
			time = "2021";
		}
		if (!LimitUtil.getRate()) {
			return ResponseObj.setResultCode(ResultCode.FAIL_COMMIT_LOCK);
		}
		try {
			List<IdeaVO> ideaVOList = (List<IdeaVO>) RedisClient.get(IDEA_KEY + time,
					new TypeReference<List<IdeaVO>>() {
					});
			if (!CollectionUtils.isEmpty(ideaVOList)) {
				result.setData(ideaVOList);
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("time", time);
				map.put("offset", 0);
				map.put("limit", 2000);
				List<Idea> ideaList = ideaService.getIdeas(map);
				if (!CollectionUtils.isEmpty(ideaList)) {
					ideaVOList = Lists.newArrayList();
					List<String> tempList = Lists.newArrayList();
					for (Idea idea : ideaList) {
						if (null != map.get(idea.getTime())) {
							List<Idea> temp = (List<Idea>)map.get(idea.getTime());
							temp.add(idea);
						} else {
							tempList.add(idea.getTime());
							map.put(idea.getTime(), Lists.newArrayList(idea));
						}
					}
					for (String temp : tempList) {
						IdeaVO ideaVO = new IdeaVO();
						ideaVO.setTime(temp);
						ideaVO.setIdeaList((List<Idea>)map.get(temp));
						ideaVOList.add(ideaVO);
					}
					result.setData(ideaVOList);
					RedisClient.set(IDEA_KEY + time, ideaVOList, 60);
				}
			}
		} catch (Exception e) {
			log.error("getIdeaList is exception:{}", e.toString());
			return ResponseObj.setResultCode(ResultCode.FAIL_DATA_WRONG);
		}
		return result;
	}

}
