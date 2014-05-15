package com.strapin.Enum;

import org.json.JSONObject;

import com.strapin.network.KlHttpClient;

public enum URL {
	/*SEND_CURRENT_LOCATION("http://23.239.206.137/send_current_location.php"),
	SIGNUP("http://23.239.206.137/sign_up.php"),
	LOGIN("http://23.239.206.137/login_new.php"),
	COUNT_PENDING_FRIEND("http://23.239.206.137/count_pending_friend.php"),
	ADD_FRIEND("http://23.239.206.137/add_friend_request.php"),
	STATUS_TRACK_TOOGLE("http://23.239.206.137/change_track.php"),
	ACCEPT_FRIEND_REQ("http://23.239.206.137/accept_friend.php"),
	MESSAGE("http://23.239.206.137/new_chat_history.php"),
	ACTIVE_APP_USERS("http://23.239.206.137/app_user.php"),
	ADD_MEET_UP_LOCATION("http://23.239.206.137/add_meet_up.php"),
	MEET_UP_MERKER_LIST("http://23.239.206.137/list_meetup.php"),
	CHAT("http://23.239.206.137/chat.php"),
	MEET_UP_DETELE("http://23.239.206.137/del_meet_up.php"),
	FRIEND_DELETE("http://23.239.206.137/delete_friend.php"),
	PENDING_FRIEND_REQUEST("http://23.239.206.137/pending_friend.php"),
	CHAT_HISTORY("http://23.239.206.137/chat_history.php"),
	SEND_MESSAGE("http://23.239.206.137/send_message.php"),
	GET_LOCATION("http://23.239.206.137/track.php"),
	FRIEND_LIST("http://23.239.206.137/snowmada_friend.php"),
	SKI_PATROL("http://23.239.206.137/ski_patrol.php"),
	IMAGE_PATH("http://23.239.206.137/uploads/"),
	GALLERY_IMG_PATH("http://23.239.206.137/uploads/gallery/"),
	UPDATE_PROFILE("http://23.239.206.137/update_profile_demo.php"),
	GALLERY_UPLOAD("http://23.239.206.137/gallery_upload.php"),
	PROFILE("http://23.239.206.137/profile_info.php"),
	VIEW_COMMENTS("http://23.239.206.137/view_comment.php"),
	SUBMIT_COMMENTS("http://23.239.206.137/submit_comment.php"),
	PROFILE_WITH_COMMENTS("http://23.239.206.137/profile_info_comment.php"),
	SIGNAL_STATUS("http://23.239.206.137/device_signal_status.php"),
	PROFILE_IMAGE_UPDATE("http://23.239.206.137/profile_image_update.php"),
	PROFILE_INFO_UPDATE("http://23.239.206.137/profile_update_demo.php");*/
    
    
        SEND_CURRENT_LOCATION("http://23.239.206.137/send_current_location.php"),
	SIGNUP("http://23.239.206.137/sign_up.php"),
	LOGIN("http://23.239.206.137/login_new.php"),
	COUNT_PENDING_FRIEND("http://23.239.206.137/count_pending_friend.php"),
	ADD_FRIEND("http://23.239.206.137/add_friend_request.php"),
	STATUS_TRACK_TOOGLE("http://23.239.206.137/change_track.php"),
	ACCEPT_FRIEND_REQ("http://23.239.206.137/accept_friend.php"),
	MESSAGE("http://23.239.206.137/new_chat_history.php"),
	ACTIVE_APP_USERS("http://23.239.206.137/app_user.php"),
	ADD_MEET_UP_LOCATION("http://23.239.206.137/add_meet_up.php"),
	MEET_UP_MERKER_LIST("http://23.239.206.137/list_meetup.php"),
	CHAT("http://23.239.206.137/chat.php"),
	MEET_UP_DETELE("http://23.239.206.137/del_meet_up.php"),
	FRIEND_DELETE("http://23.239.206.137/delete_friend.php"),
	PENDING_FRIEND_REQUEST("http://23.239.206.137/pending_friend.php"),
	CHAT_HISTORY("http://23.239.206.137/chat_history.php"),
	SEND_MESSAGE("http://23.239.206.137/send_message.php"),
	GET_LOCATION("http://23.239.206.137/track.php"),
	FRIEND_LIST("http://23.239.206.137/snowmada_friend.php"),
	SKI_PATROL("http://23.239.206.137/ski_patrol.php"),
	IMAGE_PATH("http://23.239.206.137/uploads/"),
	GALLERY_IMG_PATH("http://23.239.206.137/uploads/gallery/"),
	UPDATE_PROFILE("http://23.239.206.137/update_profile_demo.php"),
	GALLERY_UPLOAD("http://23.239.206.137/gallery_upload.php"),
	PROFILE("http://23.239.206.137/profile_info.php"),
	VIEW_COMMENTS("http://23.239.206.137/view_comment.php"),
	SUBMIT_COMMENTS("http://23.239.206.137/submit_comment.php"),
	PROFILE_WITH_COMMENTS("http://23.239.206.137/profile_info_comment.php"),
	SIGNAL_STATUS("http://23.239.206.137/device_signal_status.php"),
	PROFILE_IMAGE_UPDATE("http://23.239.206.137/profile_image_update.php"),
	PROFILE_INFO_UPDATE("http://23.239.206.137/profile_update_demo.php"),
	GOOD_DEALS("http://23.239.206.137/banner_advertisements.php"),
	GCM_REGITER("http://23.239.206.137/register.php"),
	DEALS_DETAILS("http://23.239.206.137/advt_details.php");
	
	URL(String ob) {
		this.url = ob;
	}
	public String getUrl() {
		return url;
	}
	public String url;
}
