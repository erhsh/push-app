package com.blackcrystalinfo.push.pusher.msg.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
import com.baidu.yun.channel.model.PushBroadcastMessageResponse;
import com.baidu.yun.channel.model.PushTagMessageRequest;
import com.baidu.yun.channel.model.PushTagMessageResponse;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.blackcrystalinfo.push.data.msg.AMsgData;
import com.blackcrystalinfo.push.data.msg.impl.BaiduMsgData;
import com.blackcrystalinfo.push.data.msg.impl.BaiduMsgData.DeviceType;
import com.blackcrystalinfo.push.exception.PushPuserException;
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
	protected void pushMsg(AMsgData msgData) throws PushPuserException {
		if (msgData instanceof BaiduMsgData) {
			BaiduMsgData baiduMsgData = (BaiduMsgData) msgData;

			try {
				// 确定给哪种设备推送，默认给android，iOS推
				DeviceType deviceType = baiduMsgData.getDeviceType();
				if (null == deviceType) {
					switch (baiduMsgData.getPushType()) {
					case UNICAST:
						pushUnicastMessage(baiduMsgData, DeviceType.ANDROID);
						pushUnicastMessage(baiduMsgData, DeviceType.IOS);
						break;
					case TAG:
						pushTagMessage(baiduMsgData, DeviceType.ANDROID);
						pushTagMessage(baiduMsgData, DeviceType.IOS);
						break;
					case BROADCAST:
						pushBroadcastMessage(baiduMsgData, DeviceType.ANDROID);
						pushBroadcastMessage(baiduMsgData, DeviceType.IOS);
						break;
					default:
						logger.error("Unsurpport MessageType {}, discard it.");
						break;
					}
				} else {
					switch (baiduMsgData.getPushType()) {
					case UNICAST:
						pushUnicastMessage(baiduMsgData, deviceType);
						break;
					case TAG:
						pushTagMessage(baiduMsgData, deviceType);
						break;
					case BROADCAST:
						pushBroadcastMessage(baiduMsgData, deviceType);
						break;
					default:
						logger.error("Unsurpport MessageType {}, discard it.");
						break;
					}
				}

			} catch (ChannelClientException e) {
				// 处理客户端错误异常
				logger.error("Channel Client Exception", e);
				throw new PushPuserException("Client exception!!! ", e);
			} catch (ChannelServerException e) {
				// 处理服务端错误异常
				logger.error(String.format(
						"request_id: %d, error_code: %d, error_message: %s",
						e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
				throw new PushPuserException("Server exception!!!", e);
			}

		}

	}

	private void pushUnicastMessage(BaiduMsgData baiduMsgData,
			DeviceType deviceType) throws ChannelClientException,
			ChannelServerException {
		// 创建请求类对象
		PushUnicastMessageRequest request = new PushUnicastMessageRequest();
		request.setUserId(baiduMsgData.getUserId());
		request.setChannelId(baiduMsgData.getChannelId());
		request.setMessage(baiduMsgData.getMessage());
		request.setMessageType(baiduMsgData.getMessageType().getValue());
		request.setDeviceType(baiduMsgData.getDeviceType().getValue());

		// 调用pushMessage接口
		logger.info(
				"Push request >>>: uid={}, cid={}, msg={}, msgType={}, devType={}",
				request.getUserId(), request.getChannelId(),
				request.getMessage(), request.getMessageType(),
				request.getDeviceType());

		PushUnicastMessageResponse response = channelClient
				.pushUnicastMessage(request);

		logger.info("Push amount <<<: " + response.getSuccessAmount());
	}

	private void pushTagMessage(BaiduMsgData baiduMsgData, DeviceType deviceType)
			throws ChannelClientException, ChannelServerException {
		// 创建请求类对象
		PushTagMessageRequest request = new PushTagMessageRequest();
		request.setTagName(baiduMsgData.getTag());
		request.setMessage(baiduMsgData.getMessage());
		request.setMessageType(baiduMsgData.getMessageType().getValue());
		request.setDeviceType(deviceType.getValue());
		request.setDeployStatus(baiduMsgData.getDeployStatus());

		// 调用pushMessage接口
		logger.info("Push request >>>: tag={}, msg={}, msgType={}, devType={}",
				request.getTagName(), request.getMessage(),
				request.getMessageType(), request.getDeviceType());

		PushTagMessageResponse response = channelClient.pushTagMessage(request);

		logger.info("Push amount <<<: " + response.getSuccessAmount());
	}

	private void pushBroadcastMessage(BaiduMsgData baiduMsgData,
			DeviceType deviceType) throws ChannelClientException,
			ChannelServerException {
		// 创建请求类对象
		PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
		request.setMessage(baiduMsgData.getMessage());
		request.setMessageType(baiduMsgData.getMessageType().getValue());
		request.setDeviceType(deviceType.getValue());

		// 调用pushMessage接口
		logger.info("Push request >>>:  msg={}, msgType={}, devType={}",
				request.getMessage(), request.getMessageType(),
				request.getDeviceType());

		PushBroadcastMessageResponse response = channelClient
				.pushBroadcastMessage(request);

		logger.info("Push amount <<<: " + response.getSuccessAmount());
	}

}
