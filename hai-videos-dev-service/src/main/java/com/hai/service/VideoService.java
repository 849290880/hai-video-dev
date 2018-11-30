package com.hai.service;

import com.hai.pojo.Videos;
import com.hai.pojo.vo.VideoInfoVO;
import com.hai.pojo.vo.VideosVO;
import com.hai.util.PageResult;

public interface VideoService {
	
	/**
	 * @description 将用户上传的视频保存在数据库中
	 * @param video
	 */
	public void addVideo(Videos video);
	
	/**
	 * 分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param isSearch
	 * @param word
	 * @return
	 */
	public PageResult<VideosVO> queryAll(int pageNumber,int pageSize,String isSearch,String word);
		
	/**
	 * 点赞业务: 点赞 user_fans 增加一条记录  user_like_video增加一条记录  更新user表   更新video表
	 * @param videosVO
	 * @param loginUserId
	 */
	public void likeVedio(VideosVO videosVO,String loginUserId);
	
	/**
	 * 取消点赞
	 * @param videosVO
	 * @param loginUserId
	 */
	public void unlikeVedio(VideosVO videosVO, String loginUserId);
	
	/**
	 * 查询视频页面所需要的信息
	 * @return
	 */
	public VideoInfoVO<Void> showVideoInfo(String userId,String videoId);
	
	/**
	 * 根据发布者的Id查询到对应的视频
	 * @param userId
	 * @return
	 */
	public PageResult<VideosVO> showUserVideo(String publisher,int pageNumber,int pageSize);
	
	/**
	 *根据发布者的Id查询收藏的视频
	 * @param publisherId
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public PageResult<VideosVO> showUserCareVideo(String publisherId,int pageNumber,int pageSize);
}
