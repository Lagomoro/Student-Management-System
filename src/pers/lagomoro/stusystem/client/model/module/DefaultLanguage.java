package pers.lagomoro.stusystem.client.model.module;

public class DefaultLanguage extends Language{
	
	public DefaultLanguage(){
		super();
		this.create();
	}

	@Override
	protected void initialize() {
		this.setPackname("默认 - 简体中文");
		this.setAuthor("Lagomoro");
		this.setDescription("系统默认语言，无法删除。");
		this.setVersion("V1.0");
		this.setSubmit("2019/03/24");
	}
	
	private void create() {
		this.add("System->FontSize::42"  , "初号");
		this.add("System->FontSize::36"  , "小初");
		this.add("System->FontSize::26"  , "一号");
		this.add("System->FontSize::24"  , "小一");
		this.add("System->FontSize::22"  , "二号");
		this.add("System->FontSize::18"  , "小二");
		this.add("System->FontSize::16"  , "三号");
		this.add("System->FontSize::15"  , "小三");
		this.add("System->FontSize::14"  , "四号");
		this.add("System->FontSize::12"  , "小四");
		this.add("System->FontSize::10.5", "五号");
		this.add("System->FontSize::9"   , "小五");
		this.add("System->FontSize::7.5" , "六号");
		this.add("System->FontSize::6.5" , "小六");
		this.add("System->FontSize::5.5" , "七号");
		this.add("System->FontSize::5"   , "八号");

		this.add("UI->Window::ToolTip->Minimize", "最小化");		
		this.add("UI->Window::ToolTip->Close"   , "关闭");		
		
		this.add("UI->Window->Login::Title"         , "登录MoroChat");
		this.add("UI->Window->Login::Login/Register", "登录");
		this.add("UI->Window->Login::UserName"      , "用户名");
		this.add("UI->Window->Login::Password"      , "密码");

		this.add("UI->Window->Login::Warning->UserNameEmpty"    , "用户名不能为空！");
		this.add("UI->Window->Login::Warning->PasswordEmpty"    , "密码不能为空！");
		this.add("UI->Window->Login::Warning->UserNameUndefined", "用户名未注册！");
		this.add("UI->Window->Login::Warning->PasswordInvaild"  , "密码错误！");
		this.add("UI->Window->Login::Hint->LoginHint", "正在登录中 请稍候……");
		
		this.add("UI->Window->Child::Title" , "副窗口");

		this.add("UI->Window->Main::Title", "MoroChat - 班级管理系统");
		this.add("UI->Window->Main::Home"   , "主页");
		this.add("UI->Window->Main::Class"  , "班级");
		this.add("UI->Window->Main::Message", "信息");
		this.add("UI->Window->Main::Vote"   , "投票");
		this.add("UI->Window->Main::Perusal", "传阅");
		this.add("UI->Window->Main::Share"  , "文件");
		this.add("UI->Window->Main::Game"   , "游戏");
		this.add("UI->Window->Main::Option" , "设置");
		this.add("UI->Window->Main::About"  , "关于");
		
		this.add("UI->Window->Main::ChooseMusic", "选择要播放的音乐");
		this.add("UI->Window->Main::MusicFliter", "波形文件 (*.wav)");
		this.add("UI->Window->Main::Approve"    , "确定");
		
		this.add("UI->Scene->Home::Title"            , "主页");
		this.add("UI->Scene->Home::Description"      , "欢迎来到MoroChat！");
		this.add("UI->Scene->Home::Hint->Message"    , "您有%条未读消息：");
		this.add("UI->Scene->Home::Hint->MessageOver", "所有消息都处理完毕。");
		this.add("UI->Scene->Home::Hint->Notice"     , "您有%个未读公告：");
		this.add("UI->Scene->Home::Hint->NoticeOver" , "没有最新的公告了。");
		this.add("UI->Scene->Home::Hint->Vote"       , "有%个待您参与的投票：");
		this.add("UI->Scene->Home::Hint->VoteOver"   , "所有投票都参与完毕。");
		this.add("UI->Scene->Home::Hint->Persual"    , "有文档等待您的传阅：");
		this.add("UI->Scene->Home::Hint->PersualOver", "没有需要您处理的传阅。");
		this.add("UI->Scene->Home::Hint->Share"      , "上传了%个新文件：");
		this.add("UI->Scene->Home::Hint->ShareOver"  , "没有新文件。");
		this.add("UI->Scene->Home::Hint->Game"       , "我的小游戏战绩：");
		this.add("UI->Scene->Home::Hint->Done"       , "干得不错！");
		this.add("UI->Scene->Home::Hint->GoFind"     , "点此查看");

		this.add("UI->Scene->Class::Title"        , "班级公告");
		this.add("UI->Scene->Class::NowClass"     , "您未加入班级");
		this.add("UI->Scene->Class::ClassChat"    , "进入群聊");
		this.add("UI->Scene->Class::MemberList"   , "成员列表");
		this.add("UI->Scene->Class::ReleaseNotice", "发布公告");
		this.add("UI->Scene->Class::ToNotice"     , "发布公告");
		
		this.add("UI->Scene->Message::Title"      , "信息");
		this.add("UI->Scene->Message::Description", "处理您的未读信息！");

		this.add("UI->Scene->Vote::Title"       , "投票");
		this.add("UI->Scene->Vote::Description" ,"为班级活动投票！");
		
		this.add("UI->Scene->Perusal::Title"         , "传阅评价");
		this.add("UI->Scene->Perusal::Description"   , "对同学们的文档发表观点！");	
		this.add("UI->Scene->Perusal::ReleasePerusal", "发布传阅");
		this.add("UI->Scene->Perusal::ToPerusal"     , "发布传阅");
		this.add("UI->Scene->Perusal::StartVote"     , "请投票，最多选择 %s 项，已选 %s 项。");
		this.add("UI->Scene->Perusal::GetVote"       , "已有 %s 人参与投票。");
		this.add("UI->Scene->Perusal::NoVote"        , "传阅发起人未设置投票。");
		this.add("UI->Scene->Perusal::StartComment"  , "请写下你的评论：");
		this.add("UI->Scene->Perusal::SendVote"      , "确认投票");
		this.add("UI->Scene->Perusal::SendComment"   , "发送评论");
		
		this.add("UI->Scene->Share::Title"      , "文件分享");
		this.add("UI->Scene->Share::Description", "在这里和同学们一起分享文件！");
		this.add("UI->Scene->Share::UploadFile" , "上传文件");
		this.add("UI->Scene->Share::ChooseFile" , "请选择上传文件");
		this.add("UI->Scene->Share::ChooseDown" , "请选择下载目录");
		this.add("UI->Scene->Share::Approve"    , "确定");
		this.add("UI->Scene->Share::Verifing"   , "等待服务器响应……");
		this.add("UI->Scene->Share::Uploading"  , "正在上传文件……");
		this.add("UI->Scene->Share::Downloading", "正在下载文件……");
		this.add("UI->Scene->Share::Calculating", "正在计算MD5，请稍侯……");

		this.add("UI->Scene->Game::Title"      , "小游戏");
		this.add("UI->Scene->Game::Description", "开始一场快乐的游戏吧！");
		this.add("UI->Scene->Game::Paint&Guess", "你画我猜");
		this.add("UI->Scene->Game::StartGame"  , "点击前往");

		this.add("UI->Scene->Option::Title"         , "用户设置");
		this.add("UI->Scene->Option::Description"   , "为用户带来绝佳的使用体验");
		this.add("UI->Scene->Option::UserInfoChange", "修改用户信息");
		this.add("UI->Scene->Option::LanguageOutput", "导出空白模板");
		this.add("UI->Scene->Option::LanguageOption", "语言包选择");
		this.add("UI->Scene->Option::Approve"       , "确定");
		
		this.add("UI->Scene->About::Title"       , "关于软件");
		this.add("UI->Scene->About::Version"     , "版本：");
		this.add("UI->Scene->About::Author"      , "作者：");
		this.add("UI->Scene->About::Organization", "组织：");

		this.add("UI->Scene->ChatRoom::Send"        , "发送");
		this.add("UI->Scene->ChatRoom::Withdraw"    , "该消息已被撤回");
		this.add("UI->Scene->ChatRoom::WithdrawItem", "撤回");
	
		this.add("UI->Scene->NoticeEditor::Title"        , "标题");
		this.add("UI->Scene->NoticeEditor::Send"         , "发送");
		this.add("UI->Scene->NoticeEditor::ChoosePicture", "请选择图片");
		this.add("UI->Scene->NoticeEditor::PictureFliter", "图像文件 (*.jpg *.png)");
		this.add("UI->Scene->NoticeEditor::ChooseHTML"   , "请选择文档");
		this.add("UI->Scene->NoticeEditor::HTMLFliter"   , "HTML文档 (*.htm *.html)");
		this.add("UI->Scene->NoticeEditor::Approve"      , "确定");

		this.add("UI->Scene->PersualEditor::VoteNumber", "最多允许选择");
		this.add("UI->Scene->PersualEditor::NewChoose" , "新增选项");
		this.add("UI->Scene->PersualEditor::ChooseText", "填写选项");

		this.add("UI->Scene->MemberList::Title"      , "成员列表");
		this.add("UI->Scene->MemberList::Description", "查看全部班级成员");
		this.add("UI->Scene->MemberList::Admin"      , "（管理员）");
		
		this.add("UI->Scene->PaintAndGuess::ActSmoothness", "应用平滑算法");
		this.add("UI->Scene->PaintAndGuess::StringCount"  , "%d个字");
		this.add("UI->Scene->PaintAndGuess::Loading"      , "等待玩家加入……");
		this.add("UI->Scene->PaintAndGuess::Answer"       , "答案：%s");
		this.add("UI->Scene->PaintAndGuess::Undo"         , "撤销（长按清屏）");
		this.add("UI->Scene->PaintAndGuess::Redo"         , "重做（长按恢复）");

		this.add("UI->Scene->PaintAndGuessGameRoom::Title"      , "房间列表-你画我猜");
		this.add("UI->Scene->PaintAndGuessGameRoom::Description", "你画我猜小游戏的房间列表");
		this.add("UI->Scene->PaintAndGuessGameRoom::NewGameRoom", "新建房间");
		
		this.add("UI->Scene->UserInformationChange::Title"             , "修改用户信息");
		this.add("UI->Scene->UserInformationChange::Description"       , "在这里修改你的基本信息！");
		this.add("UI->Scene->UserInformationChange::Username"          , "用户名：");
		this.add("UI->Scene->UserInformationChange::Nickname"          , "昵称：");
		this.add("UI->Scene->UserInformationChange::ChangeNickname"    , "修改昵称");
		this.add("UI->Scene->UserInformationChange::Classname"         , "所属班级：");
		this.add("UI->Scene->UserInformationChange::OldPassWord"       , "旧密码：");
		this.add("UI->Scene->UserInformationChange::NewPassWord"       , "新密码：");
		this.add("UI->Scene->UserInformationChange::ConfirmNewPassWord", "确认新密码：");
		this.add("UI->Scene->UserInformationChange::ChangePassWord"    , "修改密码");
		this.add("UI->Scene->UserInformationChange::ProfileImage"      , "头像：");
		this.add("UI->Scene->UserInformationChange::ChangeImage"       , "修改头像");
		this.add("UI->Scene->UserInformationChange::Admin"             , "（管理员）");
		this.add("UI->Scene->UserInformationChange::PasswordNotEqual"  , "两次输入的密码不一致！");

//		================================================================================
		
		this.add("UI->Window->NoticeEditor::Title", "公告编辑器");
		
		this.add("UI->Window->NoticeEditor::ToolTip->ImportHTML"   , "导入文件");
		this.add("UI->Window->NoticeEditor::ToolTip->InsertPicture", "插入图片");
		this.add("UI->Window->NoticeEditor::ToolTip->Undo"         , "撤销");
		this.add("UI->Window->NoticeEditor::ToolTip->Redo"         , "重做");
		
		this.add("UI->Window->NoticeEditor::ToolTip->Bold"         , "加粗");
		this.add("UI->Window->NoticeEditor::ToolTip->Italic"       , "斜体");
		this.add("UI->Window->NoticeEditor::ToolTip->Underline"    , "下划线");
		this.add("UI->Window->NoticeEditor::ToolTip->StrikeThrough", "删除线");
		this.add("UI->Window->NoticeEditor::ToolTip->Subscript"    , "下标");
		this.add("UI->Window->NoticeEditor::ToolTip->Superscript"  , "上标");
		
//		this.add("UI->Window->NoticeEditor::ToolTip->Fontface"     , "字体");
//		this.add("UI->Window->NoticeEditor::ToolTip->Fontsize"     , "字号");
//
		this.add("UI->Window->NoticeEditor::ToolTip->AddSize"      , "增大字号");
		this.add("UI->Window->NoticeEditor::ToolTip->MinusSize"    , "减小字号");
		this.add("UI->Window->NoticeEditor::ToolTip->FontColor"    , "字体颜色");
		
		this.add("UI->Window->NoticeEditor::ToolTip->LeftAlign"    , "左对齐");
		this.add("UI->Window->NoticeEditor::ToolTip->RightAlign"   , "右对齐");
		this.add("UI->Window->NoticeEditor::ToolTip->CenterAlign"  , "居中对齐");
		this.add("UI->Window->NoticeEditor::ToolTip->Justified"    , "两端对齐");
	}
	
}
