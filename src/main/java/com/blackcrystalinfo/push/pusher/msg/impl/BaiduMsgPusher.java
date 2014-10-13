package com.blackcrystalinfo.push.pusher.msg.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.blackcrystalinfo.push.data.msg.AMsgData;
import com.blackcrystalinfo.push.data.msg.impl.BaiduMsgData;
import com.blackcrystalinfo.push.pusher.msg.AMsgPusher;
import com.blackcrystalinfo.push.utils.Constants;

public class BaiduMsgPusher extends AMsgPusher {

	private static final Logger logger = LoggerFactory
			.getLogger(BaiduMsgPusher.class);

	private BaiduChannelClient channelClient;

	public BaiduMsgPusher() {
		this(Constants.API_KEY, Constants.SECRET_KEY);
	}

	public BaiduMsgPusher(String apiKey, String secretKey) {
		// 设置developer平台的ApiKey/SecretKey
		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);
		channelClient = new BaiduChannelClient(pair);
	}

	@Override
	protected void pushMsg(AMsgData msgData) {
		if (msgData instanceof BaiduMsgData) {
			BaiduMsgData baiduMsgData = (BaiduMsgData) msgData;

			try {
				// 创建请求类对象
				PushUnicastMessageRequest request = new PushUnicastMessageRequest();
				request.setDeviceType(baiduMsgData.getDeviceType());
				request.setChannelId(baiduMsgData.getChannelId());
				request.setUserId(baiduMsgData.getUserId());
				request.setMessageType(baiduMsgData.getMessageType());
				request.setMessage(baiduMsgData.getMessage());

				// 调用pushMessage接口
				PushUnicastMessageResponse response = channelClient
						.pushUnicastMessage(request);

				// 认证推送成功
				logger.info("push amount : " + response.getSuccessAmount());

			} catch (ChannelClientException e) {
				// 处理客户端错误异常
				logger.error("Channel Client Exception", e);
			} catch (ChannelServerException e) {
				// 处理服务端错误异常
				logger.error(String.format(
						"request_id: %d, error_code: %d, error_message: %s",
						e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
			}

		}

	}

}
