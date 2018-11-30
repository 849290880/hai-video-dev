package com.hai.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hai.mapper.SearchRecordsMapper;
import com.hai.mapper.UsersLikeVideosMapper;
import com.hai.mapper.UsersMapper;
import com.hai.mapper.VideosMapper;
import com.hai.mapper.VideosVOMapper;
import com.hai.pojo.SearchRecords;
import com.hai.pojo.UsersLikeVideos;
import com.hai.pojo.Videos;
import com.hai.pojo.vo.VideoInfoVO;
import com.hai.pojo.vo.VideosVO;
import com.hai.service.VideoService;
import com.hai.util.PageResult;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class VideoServiceImpl implements VideoService{

	@Autowired
	private VideosMapper videosMapper;
	
	@Autowired
	private VideosVOMapper videosVOMapper;
	
	@Autowired
	private SearchRecordsMapper searchRecordsMapper;
	
	@Autowired
	private UsersLikeVideosMapper usersLikeVideosMapper;
	
	@Autowired
	private UsersMapper userMapper;
	
	@Autowired
	private Sid sid;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void addVideo(Videos video) {
		videosMapper.insertSelective(video);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public PageResult<VideosVO> queryAll(int pageNumber, int pageSize,String isSearch,String word) {
		//开始分页
		PageHelper.startPage(pageNumber, pageSize);
		
		//判断是否为模糊查询
		if("1".equals(isSearch)&&isSearch!=null&&StringUtils.isNotBlank(word)) {
			SearchRecords searchRecords = new SearchRecords();
			searchRecords.setId(sid.nextShort());
			searchRecords.setContent(word);
			searchRecordsMapper.insert(searchRecords);
		}else {
			//非模糊搜索
			word = null;
		}
		
		List<VideosVO> list=videosVOMapper.selectAllVideosVO(word);
		PageInfo<VideosVO> pageInfo = new PageInfo<>(list);
		
		
		
		PageResult<VideosVO> pr =new PageResult<VideosVO>();
		pr.setCurrentPage(pageInfo.getPageNum());
		pr.setObject(list);
		pr.setPageTotal(pageInfo.getPages());
		pr.setRecordNumber(pageInfo.getTotal());
		return pr;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void likeVedio(VideosVO videosVO, String loginUserId) {
		//向 users_Like_videos 插入一条数据
		UsersLikeVideos userLikeVideos = new UsersLikeVideos();
		userLikeVideos.setId(sid.nextShort());
		userLikeVideos.setUserId(loginUserId);
		userLikeVideos.setVideoId(videosVO.getId());
		usersLikeVideosMapper.insert(userLikeVideos);
		
//		//向  插入一条数据
//		UsersFans usersFans = new UsersFans();
//		usersFans.setId(sid.nextShort());
//		usersFans.setUserId(videosVO.getUserId());
//		usersFans.setFanId(loginUserId);
//		usersFansMapper.insert(usersFans);
		
		//更新user表
		userMapper.addReciveLikeCounts(videosVO.getUserId());
		
		//更新video表
		videosVOMapper.addCounts(videosVO.getId());
		System.out.println("点赞完成...........");
	}
		
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void unlikeVedio(VideosVO videosVO, String loginUserId) {
		//向 users_Like_videos 删除一条数据
		Example userLikeVideosExample = new Example(UsersLikeVideos.class);
		Criteria userLikeVideosCriteria=userLikeVideosExample.createCriteria();
		userLikeVideosCriteria.andEqualTo("userId", loginUserId).andEqualTo("videoId", videosVO.getId());
		usersLikeVideosMapper.deleteByExample(userLikeVideosExample);
		
//		//向  users_fans删除一条数据
//		Example usersFansExample = new Example(UsersFans.class);
//		Criteria usersFansCriteria=userLikeVideosExample.createCriteria();
//		usersFansCriteria.andEqualTo("userId", videosVO.getUserId()).andEqualTo("videoId", loginUserId);
//		usersFansMapper.deleteByExample(usersFansExample);
		
		//更新user表
		userMapper.reduceReciveLikeCounts(videosVO.getUserId());
		
		//更新video表
		videosVOMapper.reduceCounts(videosVO.getId());
	}

	@Override
	public VideoInfoVO<Void> showVideoInfo(String userId,String videoId) {
		
		System.out.println("userId"+userId);
		System.out.println("videoId"+videoId);
		//根据用户的Id和视频的id查询是否点赞
		Example example = new Example(UsersLikeVideos.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", userId).andEqualTo("videoId", videoId);
		// select * from users_like_videos where user_id = '181023B7ABCH77R4' and video_id = '180510CCCK8A6S14';
//		List<UsersLikeVideos> list=usersLikeVideosMapper.selectByExample(example);
		UsersLikeVideos usersLikeVideos=usersLikeVideosMapper.selectOneByExample(example);
//		System.out.println(list.size());
		boolean result =false;
		if(usersLikeVideos!=null) {
			result =true;
		}
		return new VideoInfoVO<Void>().addIsClickResult(result);
	}

	@Override
	public PageResult<VideosVO> showUserVideo(String publisherId,int pageNumber,int pageSize) {
		PageHelper.startPage(pageNumber, pageSize);
		//这里改成videoVO
//		Example example = new Example(Videos.class);
//		Criteria criteria=example.createCriteria();
//		criteria.andEqualTo("userId",publisherId);
//		List<Videos> list=videosMapper.selectByExample(example);
		List<VideosVO> list=videosVOMapper.selectVideoVOByUserId(publisherId);
		PageInfo<VideosVO> pageInfo = new PageInfo<>(list);
		PageResult<VideosVO> pr = new PageResult<VideosVO>();
		pr.setPageTotal(pageInfo.getPages());
		pr.setObject(list);
		pr.setCurrentPage(pageNumber);
		pr.setRecordNumber(pageInfo.getTotal());
 		return pr;
	}

	@Override
	public PageResult<VideosVO> showUserCareVideo(String publisherId, int pageNumber, int pageSize) {
		PageHelper.startPage(pageNumber, pageSize);
		List<VideosVO> list=videosVOMapper.selectAllUserLikeVideosVO(publisherId);
		PageInfo<VideosVO> pageInfo = new PageInfo<>(list);
		PageResult<VideosVO> pr =new PageResult<>();
		pr.setCurrentPage(pageNumber);
		pr.setObject(list);
		pr.setPageTotal(pageInfo.getPages());
		pr.setRecordNumber(pageInfo.getTotal());
		return pr;
	}
}
