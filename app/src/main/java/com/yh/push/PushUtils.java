package com.yh.push;

import com.igexin.sdk.PushManager;
import com.yh.dao.MsgService;
import com.yh.utils.StringUtils;
import android.content.Context;


public class PushUtils {

	private MsgService service = null;
	
	//初始化推送SDK
	public static void init(Context context){
		PushManager.getInstance().initialize(context,PushService.class);
	}
	
	//获取客户端ID
	public static String getClientId(Context context){
		PushManager pushManager = PushManager.getInstance();
		String clientId = pushManager.getClientid(context);
		if(StringUtils.isBlank(clientId)){
			pushManager.initialize(context,PushService.class);
			clientId = pushManager.getClientid(context);
		}
		return clientId;
	}


}
