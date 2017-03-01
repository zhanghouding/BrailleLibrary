package com.sunteam.library.parse;

import org.json.JSONObject;

import com.sunteam.library.entity.UserInfoEntity;
import com.sunteam.library.utils.LogUtils;

//找回密码第一步，得到用户密码信息
public class UserGetPasswordParseResponse extends AbsParseResponse 
{
	public static final String TAG = "UserGetPasswordParseResponse";

	@Override
	public Object parseResponse(String responseStr) throws Exception 
	{
		// TODO Auto-generated method stub
		LogUtils.e(TAG,"responseStr---" + responseStr);
		
		try
		{
			JSONObject jsonObject = new JSONObject(responseStr);
			boolean result = jsonObject.optBoolean("IsSuccess");
			
			if( !result )
			{
				return	null;
			}
			
			JSONObject json = jsonObject.optJSONObject("ResultObject");
			if( null == json )
			{
				return	null;
			}
			
			UserInfoEntity entity = new UserInfoEntity();
			entity.userName = json.optString("UserName");
		    entity.realName = json.optString("RealName");
		    entity.password = json.optString("Password");
		    entity.userTypepublic = json.optString("UserTypepublic");
		    entity.securityTypepublic = json.optString("SecurityTypepublic");
		    entity.securityValuepublic = json.optString("SecurityValuepublic");
		    entity.beginDatepublic = json.optString("BeginDatepublic");
		    entity.endDatepublic = json.optString("EndDatepublic");
		    entity.stopFlagpublic = json.optString("StopFlagpublic");
		    entity.unitpublic = json.optString("Unitpublic");
		    entity.unitSysIDpublic = json.optString("UnitSysIDpublic");
		    entity.deptpublic = json.optString("Deptpublic");
		    entity.deptSysIDpublic = json.optString("DeptSysIDpublic");
		    entity.unitDeptCodepublic = json.optString("UnitDeptCodepublic");
		    entity.telephonepublic = json.optString("Telephonepublic");
		    entity.emailpublic = json.optString("Emailpublic");
		    entity.postCodepublic = json.optString("PostCodepublic");
		    entity.addresspublic = json.optString("Addresspublic");
		    entity.descriptpublic = json.optString("Descriptpublic");
		    entity.sysIDpublic = json.optString("SysIDpublic");
		    entity.disableCardNopublic = json.optString("DisableCardNopublic");
		    entity.readerCardNopublic = json.optString("ReaderCardNopublic");
		    entity.creditspublic = json.optString("Creditspublic");
		    entity.continueDayspublic = json.optString("ContinueDayspublic");
		    
	        return	entity;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
