package pers.lagomoro.stusystem.client.model.module;

public class DefaultLanguage extends Language{
	
	public DefaultLanguage(){
		super();
		this.create();
	}

	@Override
	protected void initialize() {
		this.setPackname("Ĭ�� - ��������");
		this.setAuthor("Lagomoro");
		this.setDescription("ϵͳĬ�����ԣ��޷�ɾ����");
		this.setVersion("V1.0");
		this.setSubmit("2019/03/24");
	}
	
	private void create() {
		this.add("System->FontSize::42"  , "����");
		this.add("System->FontSize::36"  , "С��");
		this.add("System->FontSize::26"  , "һ��");
		this.add("System->FontSize::24"  , "Сһ");
		this.add("System->FontSize::22"  , "����");
		this.add("System->FontSize::18"  , "С��");
		this.add("System->FontSize::16"  , "����");
		this.add("System->FontSize::15"  , "С��");
		this.add("System->FontSize::14"  , "�ĺ�");
		this.add("System->FontSize::12"  , "С��");
		this.add("System->FontSize::10.5", "���");
		this.add("System->FontSize::9"   , "С��");
		this.add("System->FontSize::7.5" , "����");
		this.add("System->FontSize::6.5" , "С��");
		this.add("System->FontSize::5.5" , "�ߺ�");
		this.add("System->FontSize::5"   , "�˺�");

		this.add("UI->Window::ToolTip->Minimize", "��С��");		
		this.add("UI->Window::ToolTip->Close"   , "�ر�");		
		
		this.add("UI->Window->Login::Title"         , "��¼MoroChat");
		this.add("UI->Window->Login::Login/Register", "��¼");
		this.add("UI->Window->Login::UserName"      , "�û���");
		this.add("UI->Window->Login::Password"      , "����");

		this.add("UI->Window->Login::Warning->UserNameEmpty"    , "�û�������Ϊ�գ�");
		this.add("UI->Window->Login::Warning->PasswordEmpty"    , "���벻��Ϊ�գ�");
		this.add("UI->Window->Login::Warning->UserNameUndefined", "�û���δע�ᣡ");
		this.add("UI->Window->Login::Warning->PasswordInvaild"  , "�������");
		this.add("UI->Window->Login::Hint->LoginHint", "���ڵ�¼�� ���Ժ򡭡�");
		
		this.add("UI->Window->Child::Title" , "������");

		this.add("UI->Window->Main::Title", "MoroChat - �༶����ϵͳ");
		this.add("UI->Window->Main::Home"   , "��ҳ");
		this.add("UI->Window->Main::Class"  , "�༶");
		this.add("UI->Window->Main::Message", "��Ϣ");
		this.add("UI->Window->Main::Vote"   , "ͶƱ");
		this.add("UI->Window->Main::Perusal", "����");
		this.add("UI->Window->Main::Share"  , "�ļ�");
		this.add("UI->Window->Main::Game"   , "��Ϸ");
		this.add("UI->Window->Main::Option" , "����");
		this.add("UI->Window->Main::About"  , "����");
		
		this.add("UI->Window->Main::ChooseMusic", "ѡ��Ҫ���ŵ�����");
		this.add("UI->Window->Main::MusicFliter", "�����ļ� (*.wav)");
		this.add("UI->Window->Main::Approve"    , "ȷ��");
		
		this.add("UI->Scene->Home::Title"            , "��ҳ");
		this.add("UI->Scene->Home::Description"      , "��ӭ����MoroChat��");
		this.add("UI->Scene->Home::Hint->Message"    , "����%��δ����Ϣ��");
		this.add("UI->Scene->Home::Hint->MessageOver", "������Ϣ��������ϡ�");
		this.add("UI->Scene->Home::Hint->Notice"     , "����%��δ�����棺");
		this.add("UI->Scene->Home::Hint->NoticeOver" , "û�����µĹ����ˡ�");
		this.add("UI->Scene->Home::Hint->Vote"       , "��%�����������ͶƱ��");
		this.add("UI->Scene->Home::Hint->VoteOver"   , "����ͶƱ��������ϡ�");
		this.add("UI->Scene->Home::Hint->Persual"    , "���ĵ��ȴ����Ĵ��ģ�");
		this.add("UI->Scene->Home::Hint->PersualOver", "û����Ҫ������Ĵ��ġ�");
		this.add("UI->Scene->Home::Hint->Share"      , "�ϴ���%�����ļ���");
		this.add("UI->Scene->Home::Hint->ShareOver"  , "û�����ļ���");
		this.add("UI->Scene->Home::Hint->Game"       , "�ҵ�С��Ϸս����");
		this.add("UI->Scene->Home::Hint->Done"       , "�ɵò���");
		this.add("UI->Scene->Home::Hint->GoFind"     , "��˲鿴");

		this.add("UI->Scene->Class::Title"        , "�༶����");
		this.add("UI->Scene->Class::NowClass"     , "��δ����༶");
		this.add("UI->Scene->Class::ClassChat"    , "����Ⱥ��");
		this.add("UI->Scene->Class::MemberList"   , "��Ա�б�");
		this.add("UI->Scene->Class::ReleaseNotice", "��������");
		this.add("UI->Scene->Class::ToNotice"     , "��������");
		
		this.add("UI->Scene->Message::Title"      , "��Ϣ");
		this.add("UI->Scene->Message::Description", "��������δ����Ϣ��");

		this.add("UI->Scene->Vote::Title"       , "ͶƱ");
		this.add("UI->Scene->Vote::Description" ,"Ϊ�༶�ͶƱ��");
		
		this.add("UI->Scene->Perusal::Title"         , "��������");
		this.add("UI->Scene->Perusal::Description"   , "��ͬѧ�ǵ��ĵ�����۵㣡");	
		this.add("UI->Scene->Perusal::ReleasePerusal", "��������");
		this.add("UI->Scene->Perusal::ToPerusal"     , "��������");
		this.add("UI->Scene->Perusal::StartVote"     , "��ͶƱ�����ѡ�� %s ���ѡ %s �");
		this.add("UI->Scene->Perusal::GetVote"       , "���� %s �˲���ͶƱ��");
		this.add("UI->Scene->Perusal::NoVote"        , "���ķ�����δ����ͶƱ��");
		this.add("UI->Scene->Perusal::StartComment"  , "��д��������ۣ�");
		this.add("UI->Scene->Perusal::SendVote"      , "ȷ��ͶƱ");
		this.add("UI->Scene->Perusal::SendComment"   , "��������");
		
		this.add("UI->Scene->Share::Title"      , "�ļ�����");
		this.add("UI->Scene->Share::Description", "�������ͬѧ��һ������ļ���");
		this.add("UI->Scene->Share::UploadFile" , "�ϴ��ļ�");
		this.add("UI->Scene->Share::ChooseFile" , "��ѡ���ϴ��ļ�");
		this.add("UI->Scene->Share::ChooseDown" , "��ѡ������Ŀ¼");
		this.add("UI->Scene->Share::Approve"    , "ȷ��");
		this.add("UI->Scene->Share::Verifing"   , "�ȴ���������Ӧ����");
		this.add("UI->Scene->Share::Uploading"  , "�����ϴ��ļ�����");
		this.add("UI->Scene->Share::Downloading", "���������ļ�����");
		this.add("UI->Scene->Share::Calculating", "���ڼ���MD5�����Ժ��");

		this.add("UI->Scene->Game::Title"      , "С��Ϸ");
		this.add("UI->Scene->Game::Description", "��ʼһ�����ֵ���Ϸ�ɣ�");
		this.add("UI->Scene->Game::Paint&Guess", "�㻭�Ҳ�");
		this.add("UI->Scene->Game::StartGame"  , "���ǰ��");

		this.add("UI->Scene->Option::Title"         , "�û�����");
		this.add("UI->Scene->Option::Description"   , "Ϊ�û��������ѵ�ʹ������");
		this.add("UI->Scene->Option::UserInfoChange", "�޸��û���Ϣ");
		this.add("UI->Scene->Option::LanguageOutput", "�����հ�ģ��");
		this.add("UI->Scene->Option::LanguageOption", "���԰�ѡ��");
		this.add("UI->Scene->Option::Approve"       , "ȷ��");
		
		this.add("UI->Scene->About::Title"       , "�������");
		this.add("UI->Scene->About::Version"     , "�汾��");
		this.add("UI->Scene->About::Author"      , "���ߣ�");
		this.add("UI->Scene->About::Organization", "��֯��");

		this.add("UI->Scene->ChatRoom::Send"        , "����");
		this.add("UI->Scene->ChatRoom::Withdraw"    , "����Ϣ�ѱ�����");
		this.add("UI->Scene->ChatRoom::WithdrawItem", "����");
	
		this.add("UI->Scene->NoticeEditor::Title"        , "����");
		this.add("UI->Scene->NoticeEditor::Send"         , "����");
		this.add("UI->Scene->NoticeEditor::ChoosePicture", "��ѡ��ͼƬ");
		this.add("UI->Scene->NoticeEditor::PictureFliter", "ͼ���ļ� (*.jpg *.png)");
		this.add("UI->Scene->NoticeEditor::ChooseHTML"   , "��ѡ���ĵ�");
		this.add("UI->Scene->NoticeEditor::HTMLFliter"   , "HTML�ĵ� (*.htm *.html)");
		this.add("UI->Scene->NoticeEditor::Approve"      , "ȷ��");

		this.add("UI->Scene->PersualEditor::VoteNumber", "�������ѡ��");
		this.add("UI->Scene->PersualEditor::NewChoose" , "����ѡ��");
		this.add("UI->Scene->PersualEditor::ChooseText", "��дѡ��");

		this.add("UI->Scene->MemberList::Title"      , "��Ա�б�");
		this.add("UI->Scene->MemberList::Description", "�鿴ȫ���༶��Ա");
		this.add("UI->Scene->MemberList::Admin"      , "������Ա��");
		
		this.add("UI->Scene->PaintAndGuess::ActSmoothness", "Ӧ��ƽ���㷨");
		this.add("UI->Scene->PaintAndGuess::StringCount"  , "%d����");
		this.add("UI->Scene->PaintAndGuess::Loading"      , "�ȴ���Ҽ��롭��");
		this.add("UI->Scene->PaintAndGuess::Answer"       , "�𰸣�%s");
		this.add("UI->Scene->PaintAndGuess::Undo"         , "����������������");
		this.add("UI->Scene->PaintAndGuess::Redo"         , "�����������ָ���");

		this.add("UI->Scene->PaintAndGuessGameRoom::Title"      , "�����б�-�㻭�Ҳ�");
		this.add("UI->Scene->PaintAndGuessGameRoom::Description", "�㻭�Ҳ�С��Ϸ�ķ����б�");
		this.add("UI->Scene->PaintAndGuessGameRoom::NewGameRoom", "�½�����");
		
		this.add("UI->Scene->UserInformationChange::Title"             , "�޸��û���Ϣ");
		this.add("UI->Scene->UserInformationChange::Description"       , "�������޸���Ļ�����Ϣ��");
		this.add("UI->Scene->UserInformationChange::Username"          , "�û�����");
		this.add("UI->Scene->UserInformationChange::Nickname"          , "�ǳƣ�");
		this.add("UI->Scene->UserInformationChange::ChangeNickname"    , "�޸��ǳ�");
		this.add("UI->Scene->UserInformationChange::Classname"         , "�����༶��");
		this.add("UI->Scene->UserInformationChange::OldPassWord"       , "�����룺");
		this.add("UI->Scene->UserInformationChange::NewPassWord"       , "�����룺");
		this.add("UI->Scene->UserInformationChange::ConfirmNewPassWord", "ȷ�������룺");
		this.add("UI->Scene->UserInformationChange::ChangePassWord"    , "�޸�����");
		this.add("UI->Scene->UserInformationChange::ProfileImage"      , "ͷ��");
		this.add("UI->Scene->UserInformationChange::ChangeImage"       , "�޸�ͷ��");
		this.add("UI->Scene->UserInformationChange::Admin"             , "������Ա��");
		this.add("UI->Scene->UserInformationChange::PasswordNotEqual"  , "������������벻һ�£�");

//		================================================================================
		
		this.add("UI->Window->NoticeEditor::Title", "����༭��");
		
		this.add("UI->Window->NoticeEditor::ToolTip->ImportHTML"   , "�����ļ�");
		this.add("UI->Window->NoticeEditor::ToolTip->InsertPicture", "����ͼƬ");
		this.add("UI->Window->NoticeEditor::ToolTip->Undo"         , "����");
		this.add("UI->Window->NoticeEditor::ToolTip->Redo"         , "����");
		
		this.add("UI->Window->NoticeEditor::ToolTip->Bold"         , "�Ӵ�");
		this.add("UI->Window->NoticeEditor::ToolTip->Italic"       , "б��");
		this.add("UI->Window->NoticeEditor::ToolTip->Underline"    , "�»���");
		this.add("UI->Window->NoticeEditor::ToolTip->StrikeThrough", "ɾ����");
		this.add("UI->Window->NoticeEditor::ToolTip->Subscript"    , "�±�");
		this.add("UI->Window->NoticeEditor::ToolTip->Superscript"  , "�ϱ�");
		
//		this.add("UI->Window->NoticeEditor::ToolTip->Fontface"     , "����");
//		this.add("UI->Window->NoticeEditor::ToolTip->Fontsize"     , "�ֺ�");
//
		this.add("UI->Window->NoticeEditor::ToolTip->AddSize"      , "�����ֺ�");
		this.add("UI->Window->NoticeEditor::ToolTip->MinusSize"    , "��С�ֺ�");
		this.add("UI->Window->NoticeEditor::ToolTip->FontColor"    , "������ɫ");
		
		this.add("UI->Window->NoticeEditor::ToolTip->LeftAlign"    , "�����");
		this.add("UI->Window->NoticeEditor::ToolTip->RightAlign"   , "�Ҷ���");
		this.add("UI->Window->NoticeEditor::ToolTip->CenterAlign"  , "���ж���");
		this.add("UI->Window->NoticeEditor::ToolTip->Justified"    , "���˶���");
	}
	
}
