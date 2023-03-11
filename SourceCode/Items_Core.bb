Const MaxItemArray% = 10

Global MaxItemAmount% = 10
Global BurntNote%
Global ItemAmount%
Global Inventory.Items[MaxItemArray] ; TODO make MaxItemAmount dynamic and establish a "inventory size" value that will cap the usable portion of the array
Global InvSelect%, SelectedItem.Items
Global ClosestItem.Items
Global LastItemID%

Type ItemTemplates
	Field name$
	Field tempname$
	Field sound%
	Field found%
	Field obj%, objpath$, parentobjpath$
	Field invimg%,invimg2%,invimgpath$
	Field imgpath$, img%
	Field isAnim%
	Field scale#
	Field tex%, texpath$
	Field isGun% = False
	Field fastPickable% = False
	Field isPlot% = False
End Type 

Function CreateItemTemplate.ItemTemplates(name$, tempname$, objpath$, invimgpath$, imgpath$, scale#, texturepath$ = "",invimgpath2$="",Anim%=0, texflags%=9)
	Local it.ItemTemplates = New ItemTemplates
	Local it2.ItemTemplates
	Local n
	
	For it2.ItemTemplates = Each ItemTemplates
		If it2\objpath = objpath And it2\obj <> 0 Then it\obj = CopyEntity(it2\obj) : it\parentobjpath=it2\objpath : Exit
	Next
	If it\obj = 0 Then
		If Anim<>0 Then
			it\obj = LoadAnimMesh_Strict(objpath)
			it\isAnim=True
		Else
			it\obj = LoadMesh_Strict(objpath)
			it\isAnim=False
		EndIf
		it\objpath = objpath
	EndIf
	it\objpath = objpath
	Local texture%
	If texturepath <> "" Then
		For it2.ItemTemplates = Each ItemTemplates
			If it2\texpath = texturepath And it2\tex<>0 Then
				texture = it2\tex
				Exit
			EndIf
		Next
		If texture=0 Then texture=LoadTexture_Strict(texturepath,texflags%,0) : it\texpath = texturepath
		EntityTexture it\obj, texture
		it\tex = texture
	EndIf  
	it\scale = scale
	ScaleEntity it\obj, scale, scale, scale, True
	For it2.ItemTemplates = Each ItemTemplates
		If it2\invimgpath = invimgpath And it2\invimg <> 0 Then
			it\invimg = it2\invimg
			If it2\invimg2<>0 Then
				it\invimg2=it2\invimg2
			EndIf
			Exit
		EndIf
	Next
	If it\invimg=0 Then
		it\invimg = LoadImage_Strict(invimgpath)
		it\invimgpath = invimgpath
		MaskImage(it\invimg, 255, 0, 255)
	EndIf
	If (invimgpath2 <> "") Then
		If it\invimg2=0 Then
			it\invimg2 = LoadImage_Strict(invimgpath2)
			MaskImage(it\invimg2,255,0,255)
		EndIf
	Else
		it\invimg2 = 0
	EndIf
	it\imgpath = imgpath
	it\tempname = tempname
	it\name = name
	it\sound = 1

	HideEntity it\obj
	
	Return it
	
End Function

Function InitItemTemplates()
	Local it.ItemTemplates,it2.ItemTemplates
	
	;! --- SECURITY STORIES ---
	
	; ~ [ATTACHMENTS]
	
	it = CreateItemTemplate("Suppressor", "suppressor", "GFX\Weapons\Models\Attachments\Suppressor.b3d","GFX\items\Icons\inv_misc.jpg","",0.03) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate("Acog Scope", "acog", "GFX\Weapons\Models\Attachments\Acog.b3d","GFX\items\Icons\inv_misc.jpg","",0.04) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate("AimPoint Sight", "aimpoint", "GFX\Weapons\Models\Attachments\AimPoint_Sight.b3d","GFX\items\Icons\inv_misc.jpg","",0.04) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate("EoTech Sight", "eotech", "GFX\Weapons\Models\Attachments\EoTech_Sight.b3d","GFX\items\Icons\inv_misc.jpg","",0.04) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate("Red Dot Sight", "reddot", "GFX\Weapons\Models\Attachments\Red_Dot_Sight.b3d","GFX\items\Icons\inv_misc.jpg","",0.04) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate("MP5 Vertical grip","verticalgrip","GFX\Weapons\Models\Attachments\MP5K_Grip.b3d","GFX\items\Icons\inv_misc.jpg","",0.04) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate("MP5 Stock", "mp5stock", "GFX\Weapons\Models\Attachments\MP5_Stock.b3d","GFX\items\Icons\inv_misc.jpg","",0.04) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate("MP5 Folding Stock", "mp5stock2", "GFX\Weapons\Models\Attachments\MP5_Folding_Stock.b3d","GFX\items\Icons\inv_misc.jpg","",0.04) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate("Laser Sight", "lasersight", "GFX\Weapons\Models\Attachments\lasersight.b3d","GFX\items\Icons\inv_misc.jpg","",0.04) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate("Extended Magazines Box", "extmag", "GFX\Weapons\Models\Attachments\extmag_box.b3d","GFX\items\Icons\inv_misc.jpg","",0.02) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate("Monitoring Unit", "mui", "GFX\Weapons\Models\Attachments\emr-p_mui.b3d","GFX\items\Icons\inv_misc.jpg","",0.01) : it\sound = 2 : it\fastPickable = True
	
	; ~ [AMMO]
	
	it = CreateItemTemplate("5 Rnd 20mm HE Grenade Magazine", "grenade_mag", "GFX\Weapons\Models\Attachments\XM29_Grenade_Mag.b3d","GFX\weapons\Icons\INVxm29_grenade.jpg","",0.02) : it\sound = 2
	it = CreateItemTemplate("Electrical Battery", "emr-p_mag", "GFX\Weapons\Models\Attachments\EMR-P_Mag.b3d", "GFX\weapons\Icons\INVemr-p_mag.jpg","",0.01) : it\sound = 2
	
	; ~ [KEY CARDS, HANDS, ETC]
	
	it = CreateItemTemplate("Silo Door Cave Card", "key_cave",  "GFX\items\key_card.x", "GFX\items\Icons\INV_key_cave.jpg", "", 0.0004,"GFX\items\key_card_cave.png")
	it = CreateItemTemplate("Storage Area Access Cave Card", "key_cave2",  "GFX\items\key_card.x", "GFX\items\Icons\INV_key_cave_2.jpg", "", 0.0004,"GFX\items\key_card_cave_2.png")
	
	; ~ [WEARIBLE]
	
	it = CreateItemTemplate("Nine-Tailed Fox Helmet", "ntf_helmet", "GFX\Items\NTF_Helmet.b3d", "GFX\items\Icons\INV_ntf_helm.jpg", "", 0.0375/2.5,"gfx\items\ntf_helm_green.png") : it\sound = 2
	it = CreateItemTemplate("Upgraded Nine-Tailed Fox Helmet", "fine_ntf_helmet", "GFX\Items\NTF_Helmet.b3d", "GFX\items\Icons\INV_ntf_helm_fine.jpg", "", 0.0375/2.5,"gfx\items\ntf_helm_blue.png") : it\sound = 2
	it = CreateItemTemplate("Ballistic Helmet", "helmet", "GFX\Items\Helmet.b3d", "GFX\items\Icons\INV_helmet.png", "", 0.0375/2.5) : it\sound = 2
	it = CreateItemTemplate("Heavy Ballistic Helmet", "finehelmet", "GFX\Items\Fine_Helmet.b3d", "GFX\items\Icons\INV_fine_helmet.png", "", 0.0375/2.5) : it\sound = 2
	it = CreateItemTemplate("Hazardous Defence Suit", "hds_suit", "GFX\Items\HDS_Suit.b3d", "GFX\items\Icons\INV_HDS.png", "", 0.0375/2.5) : it\sound = 5 : it\fastPickable = True
	it = CreateItemTemplate("SCRAMBLE Gear", "scramble", "GFX\items\SCRAMBLE_gear.b3d", "GFX\items\Icons\INV_SCRAMBLE_gear.png", "", 0.02,"GFX\items\SCRAMBLE_gear.png") : it\sound = 2
	it = CreateItemTemplate("SCRAMBLE Gear", "veryfinescramble", "GFX\items\SCRAMBLE_gear.b3d", "GFX\items\Icons\INV_SCRAMBLE_gear.png", "", 0.02,"GFX\items\SCRAMBLE_gear.png") : it\sound = 2
	
	; ~ [CONSUMABLE]
	
	it = CreateItemTemplate("Nikocado's Private Reserve Beer Bottle", "beer", "GFX\map\props\beer.x", "GFX\items\Icons\INV_beer.jpg", "", 0.04) : it\sound = 4
	
	it = CreateItemTemplate("Wrapped Candy", "red_candy", "GFX\items\candy.b3d", "GFX\items\Icons\INV_candy_red.jpg", "", 0.005,"GFX\Items\candy_red.jpg") : it\sound = 0
	it = CreateItemTemplate("Wrapped Candy", "blue_candy", "GFX\items\candy.b3d", "GFX\items\Icons\INV_candy_blue.jpg", "", 0.005,"GFX\Items\candy_blue.jpg") : it\sound = 0
	it = CreateItemTemplate("Wrapped Candy", "yellow_candy", "GFX\items\candy.b3d", "GFX\items\Icons\INV_candy_yellow.jpg", "", 0.005,"GFX\Items\candy_yellow.jpg") : it\sound = 0
	
	; ~ [MISC]
	
	it = CreateItemTemplate("Scope Battery", "scopebat", "GFX\Weapons\Models\Attachments\scope_bat.b3d", "GFX\Weapons\Icons\invscopebat.jpg","",0.008) : it\sound = 2
	it = CreateItemTemplate("Broken Helmet", "brokenhelmet", "GFX\items\helmet_broken.b3d", "GFX\items\Icons\INV_paper.jpg", "", 0.0375/2.5)
	it = CreateItemTemplate("Hazardous Defence Suit Power Cell", "hds_fuse", "GFX\items\HDS_Battery.b3d", "GFX\items\Icons\INV_hds_bat.png", "", 0.012) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate("V Coin", "vanecoin", "GFX\items\coin.b3d", "GFX\items\Icons\INV_vane_coin.jpg", "", 0.0005, "GFX\items\vane_coin.png","",0,1+2+8) : it\sound = 3
	it = CreateItemTemplate("Purple Fuse", "fuse_purple", "GFX\items\fuse.b3d", "GFX\items\Icons\INV_fuse_2.jpg", "", 0.025, "GFX\map\textures\fuse_2.png")
	
	it = CreateItemTemplate("Severed Hand", "ryan_hand", "GFX\items\Hand_left.b3d", "GFX\items\Icons\inv_misc.jpg","",0.01) : it\sound = 2
	it = CreateItemTemplate("Severed Hand", "ryan_hand2", "GFX\items\Hand_right.b3d", "GFX\items\Icons\inv_misc.jpg","",0.01) : it\sound = 2
	
	it = CreateItemTemplate("Yellow Severed Hand", "hand3", "GFX\items\severed_hand.b3d", "GFX\items\Icons\INV_hand_3.png", "", 0.04, "GFX\items\severed_hand_3.png") : it\sound = 2
	
	; ~ [PAPER]
	
	it = CreateItemTemplate("Document SCP-005", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc005.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-016", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc016.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-059", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc059.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-127", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc127.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-198", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc198.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-207", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc207.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-268", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc268.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-330", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc330.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-268 Addendum", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc268ad.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-357", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc357.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-402", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc402.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-409", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc409.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Infected SCP-409 Document", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc409_inf.png", 0.003,"GFX\items\doc409_inf.png") : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Document SCP-447", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc447.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-1033-RU", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc1033ru.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-1079", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc1079.png", 0.003) : it\sound = 0
	
	it = CreateItemTemplate("O5 Council Room Note", "paper", "GFX\items\note.x", "GFX\items\Icons\INV_note_2.jpg", "GFX\items\noteo5.jpg", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Surveillance Room Password Note", "paper", "GFX\items\note.x", "GFX\items\Icons\INV_note_2.jpg", "GFX\items\note_surveil.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Laboratory Document", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc_lab.jpg", 0.003) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Laboratory Log #1", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\lab_log_1.jpg", 0.003) : it\sound = 0 : it\isPlot = True
	
	it = CreateItemTemplate("D-7651's Note", "paper", "GFX\items\note.x", "GFX\items\Icons\INV_note_2.jpg", "GFX\items\note1499.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Note from Maynard", "paper", "GFX\items\note.x", "GFX\items\Icons\INV_note_2.jpg", "GFX\items\note_maynard.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Unknown Note", "paper", "GFX\items\note.x", "GFX\items\Icons\INV_note.jpg", "GFX\items\note_friend.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Dr. Harmann's Note", "paper", "GFX\items\note.x", "GFX\items\Icons\INV_note.jpg", "GFX\items\note016.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Dr. Singlinton's Note", "paper", "GFX\items\note.x", "GFX\items\Icons\INV_note.jpg", "GFX\items\note016_2.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Final Note", "paper", "GFX\items\note.x", "GFX\items\Icons\INV_note.jpg", "GFX\items\note016_3.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Strange Message", "paper", "GFX\items\note.x", "GFX\items\Icons\INV_note.jpg", "GFX\items\note268.png", 0.0025) : it\sound = 0 : it\isPlot = True
	
	it = CreateItemTemplate("Wolfnaya's Room Note", "paper", "GFX\items\note.x", "GFX\items\Icons\INV_note.jpg", "GFX\items\noteWolfnaya.png", 0.0025) : it\sound = 0 : it\isPlot = True
	
	it = CreateItemTemplate("Benjamin Oliver's Badge", "badge", "GFX\items\badge.x", "GFX\items\Icons\INV_badge_d9341.jpg", "GFX\items\badge_d9341.png", 0.0001, "GFX\items\badge_d9341.png") : it\isPlot = True
	
	; ~ [SCPS]
	
	it = CreateItemTemplate("SCP-005","scp005", "GFX\items\scp_005.b3d", "GFX\items\Icons\INV_scp_005.png", "", 0.04) : it\sound = 3
	it = CreateItemTemplate("SCP-016","scp016", "GFX\items\scp_016.b3d", "GFX\items\Icons\INV_scp_016.jpg", "", 0.04) : it\sound = 4
	it = CreateItemTemplate("SCP-035","scp035", "GFX\items\scp_035.b3d", "GFX\items\Icons\INV_scp_035.jpg", "", 0.02) : it\sound = 2
	it = CreateItemTemplate("SCP-059 Sample", "scp059", "GFX\items\scp_059.b3d", "GFX\items\Icons\INV_scp_059.jpg", "", 0.1)
	it = CreateItemTemplate("SCP-127","scp127","GFX\weapons\Models\scp127_Worldmodel.b3d","GFX\Weapons\Icons\INVscp127.jpg","", 0.0026) : it\sound = 66 : it\isGun% = True
	it = CreateItemTemplate("SCP-207","scp207","GFX\items\scp_207.b3d","GFX\items\Icons\INV_scp_207.png", "", 0.14) : it\sound = 4
	it = CreateItemTemplate("SCP-207 Empty Bottle","scp207empty","GFX\items\scp_207_empty.b3d","GFX\items\Icons\INV_scp_207_empty.png", "", 0.14)
	it = CreateItemTemplate("SCP-268","scp268","GFX\items\scp_268.b3d","GFX\items\Icons\INV_scp_268.png", "", 0.09, "GFX\items\scp_268.png") : it\sound = 2
	it = CreateItemTemplate("SCP-268","super268","GFX\items\scp_268.b3d","GFX\items\Icons\INV_scp_268_fine.png", "", 0.09, "GFX\items\scp_268_fine.png") : it\sound = 2
	it = CreateItemTemplate("SCP-357", "scp357", "GFX\items\scp_357.b3d","GFX\items\Icons\INV_scp_357.png","", 0.04) : it\sound = 2
	it = CreateItemTemplate("SCP-402", "scp402", "GFX\items\scp_402.b3d","GFX\items\Icons\INV_scp_402.png","",0.075) : it\sound = 3
	it = CreateItemTemplate("SCP-500", "scp500", "GFX\items\scp_500_bottle.b3d","GFX\items\Icons\INV_scp_500_bottle.jpg","",0.05) : it\sound = 2
	it = CreateItemTemplate("SCP-1033-RU", "scp1033ru", "GFX\items\scp_1033_ru.b3d", "GFX\items\Icons\INV_scp_1033_ru.png", "", 0.5,"GFX\items\scp_1033_ru.png") : it\sound = 3
    it = CreateItemTemplate("SCP-1033-RU", "super1033ru", "GFX\items\scp_1033_ru.b3d", "GFX\items\Icons\INV_scp_1033_ru_fine.png", "", 0.5,"GFX\items\scp_1033_ru_fine.png") : it\sound = 3
    it = CreateItemTemplate("SCP-1079", "scp1079", "GFX\items\scp_1079_packet.b3d","GFX\items\Icons\INV_scp_1079_packet.png","",0.18) : it\sound = 2
	it = CreateItemTemplate("SCP-1079-01", "scp1079sweet", "GFX\items\scp_1079_sweet.b3d", "GFX\items\Icons\INV_scp_1079_sweet.png", "", 0.01) : it\sound = 2
	it = CreateItemTemplate("SCP-1102-RU", "scp1102ru", "GFX\items\scp_1102_ru.b3d", "GFX\items\Icons\INV_scp_1102_ru.png", "", 0.1) : it\sound = 2
	
	; ----------------------
	
	;! --- CONTAINMENT BREACH ---
	
	; ~ [PAPER]
	
	it = CreateItemTemplate("Document SCP-008", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc008.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-012", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc012.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-035", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc035.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-049", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc049.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-079", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc079.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-427", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc427.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-682", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc682.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-860", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc860.jpg", 0.003) : it\sound = 0 	
	it = CreateItemTemplate("Document SCP-860-1", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc8601.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-895", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc895.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-173", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc173.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-372", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc372.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-096", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc096.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-500", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc500.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-714", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc714.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-513", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc513.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-939", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc939.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-966", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc966.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-970", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc970.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-1048", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc1048.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-1123", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc1123.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-1162", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc1162.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-1499", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc1499.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("Incident Report SCP-1048-A", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc1048a.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-035 Addendum", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc035ad.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-093 Recovered Materials", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc093rm.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-106", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc106.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Dr. Allok's Note", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc106_2.jpg", 0.0025) : it\sound = 0
	it = CreateItemTemplate("Recall Protocol RP-106-N", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\docRP.jpg", 0.0025) : it\sound = 0
	it = CreateItemTemplate("Drawing", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc1048.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Leaflet", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\leaflet.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Dr. L's Note", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_note.jpg", "GFX\items\docL1.jpg", 0.0025) : it\sound = 0
	it = CreateItemTemplate("Dr L's Note", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_note.jpg", "GFX\items\docL2.jpg", 0.0025) : it\sound = 0
	it = CreateItemTemplate("Blood-stained Note", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_note.jpg", "GFX\items\docL3.jpg", 0.0025) : it\sound = 0
	it = CreateItemTemplate("Dr. L's Burnt Note", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_burned_note.jpg", "GFX\items\docL4.jpg", 0.0025) : it\sound = 0
	it = CreateItemTemplate("Dr L's Burnt Note", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_burned_note.jpg", "GFX\items\docL5.jpg", 0.0025) : it\sound = 0
	it = CreateItemTemplate("Scorched Note", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_burned_note.jpg", "GFX\items\docL6.jpg", 0.0025) : it\sound = 0
	it = CreateItemTemplate("Journal Page", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\docGonzales.jpg", 0.0025) : it\sound = 0
	it = CreateItemTemplate("Log #1", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\f4.jpg", 0.004) : it\sound = 0
	it = CreateItemTemplate("Log #2", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\f5.jpg", 0.004) : it\sound = 0
	it = CreateItemTemplate("Log #3", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\f6.jpg", 0.004) : it\sound = 0
	it = CreateItemTemplate("Strange Note", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_note.jpg", "GFX\items\docStrange.jpg", 0.0025) : it\sound = 0
	it = CreateItemTemplate("Nuclear Device Document", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\docNDP.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Class D Orientation Leaflet", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\docORI.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Note from Daniel", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_note.jpg", "GFX\items\docdan.jpg", 0.0025) : it\sound = 0		
	it = CreateItemTemplate("Burnt Note", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_burned_note.jpg", "GFX\items\burnt_note.it", 0.003, "GFX\items\BurntNoteTexture.jpg") : it\img = BurntNote : it\sound = 0
	it = CreateItemTemplate("Mysterious Note", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_note.jpg", "GFX\items\sn.it", 0.003) : it\sound = 0
	it = CreateItemTemplate("Mobile Task Forces", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\docMTF.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Security Clearance Levels", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\docSC.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Object Classes", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\docOBJC.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\docRAND3.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Note", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_note.jpg", "GFX\items\docRAND2.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Notification", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_note.jpg", "GFX\items\docRAND1.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Incident Report SCP-106-0204", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\docIR106.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Origami", "misc", "GFX\items\origami.b3d", "GFX\items\Icons\INV_origami.jpg", "", 0.003) : it\sound = 0
	it = CreateItemTemplate("Clipboard", "clipboard", "GFX\items\clipboard.b3d", "GFX\items\Icons\INV_clip_board.jpg", "", 0.003, "", "GFX\items\Icons\INV_clip_board_2.jpg", 1)
	it = CreateItemTemplate("Movie Ticket", "ticket", "GFX\items\key.b3d", "GFX\items\Icons\INV_ticket.jpg", "GFX\items\ticket.png", 0.002, "GFX\items\tickettexture.png","",0,1+2+8) : it\sound = 0
	it = CreateItemTemplate("Emily Ross' Badge", "badge", "GFX\items\badge.x", "GFX\items\Icons\INV_badge.jpg", "GFX\items\badge1.jpg", 0.0001, "GFX\items\badge1.jpg")
	it = CreateItemTemplate("Disciplinary Hearing DH-S-4137-17092", "oldpaper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\dh.s", 0.003) : it\sound = 0
	it = CreateItemTemplate("Sticky Note", "paper", "GFX\items\note.x", "GFX\items\Icons\INV_note_2.jpg", "GFX\items\note682.jpg", 0.0025) : it\sound = 0
	it = CreateItemTemplate("The Modular Site Project", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\docMSP.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Research Sector-02 Scheme", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\docmap.jpg", 0.003) : it\sound = 0
	
	; ~ [KEY CARDS, HANDS, ETC]
	
	it = CreateItemTemplate("Level 1 Key Card", "key1", "GFX\items\key_card.x", "GFX\items\Icons\INV_key_1.jpg", "", 0.0004,"GFX\items\key_card_1.png")
	it = CreateItemTemplate("Level 2 Key Card", "key2", "GFX\items\key_card.x", "GFX\items\Icons\INV_key_2.jpg", "", 0.0004,"GFX\items\key_card_2.png")
	it = CreateItemTemplate("Level 3 Key Card", "key3", "GFX\items\key_card.x", "GFX\items\Icons\INV_key_3.jpg", "", 0.0004,"GFX\items\key_card_3.png")
	it = CreateItemTemplate("Level 4 Key Card", "key4", "GFX\items\key_card.x", "GFX\items\Icons\INV_key_4.jpg", "", 0.0004,"GFX\items\key_card_4.png")
	it = CreateItemTemplate("Level 5 Key Card", "key5", "GFX\items\key_card.x", "GFX\items\Icons\INV_key_5.jpg", "", 0.0004,"GFX\items\key_card_5.png")
	it = CreateItemTemplate("Key Card Omni", "key6", "GFX\items\key_card.x", "GFX\items\Icons\INV_key_omni.jpg", "", 0.0004,"GFX\items\key_card_omni.png")
	it = CreateItemTemplate("Severed Hand", "hand", "GFX\items\severed_hand.b3d", "GFX\items\Icons\INV_hand.jpg", "", 0.04,"GFX\items\severed_hand.png") : it\sound = 2
	it = CreateItemTemplate("Black Severed Hand", "hand2", "GFX\items\severed_hand.b3d", "GFX\items\Icons\INV_hand_2.jpg", "", 0.04, "GFX\items\severed_hand_2.png") : it\sound = 2
	
	; ~ [MISC]
	
	it = CreateItemTemplate("Cup", "cup", "GFX\items\cup.x", "GFX\items\Icons\INV_cup.jpg", "", 0.04) : it\sound = 2
	it = CreateItemTemplate("Empty Cup", "emptycup", "GFX\items\cup.x", "GFX\items\Icons\INV_cup.jpg", "", 0.04) : it\sound = 2	
	it = CreateItemTemplate("Electronical components", "misc", "GFX\items\electronics.x", "GFX\items\Icons\INV_electronics.jpg", "", 0.0011)
	it = CreateItemTemplate("S-NAV 300 Navigator", "nav", "GFX\items\navigator.x", "GFX\items\Icons\INV_navigator.jpg", "GFX\items\navigator.png", 0.0008)
	it = CreateItemTemplate("S-NAV Navigator", "nav", "GFX\items\navigator.x", "GFX\items\Icons\INV_navigator.jpg", "GFX\items\navigator.png", 0.0008)
	it = CreateItemTemplate("S-NAV Navigator Ultimate", "nav", "GFX\items\navigator.x", "GFX\items\Icons\INV_navigator.jpg", "GFX\items\navigator.png", 0.0008)
	it = CreateItemTemplate("S-NAV 310 Navigator", "nav", "GFX\items\navigator.x", "GFX\items\Icons\INV_navigator.jpg", "GFX\items\navigator.png", 0.0008)
	it = CreateItemTemplate("Radio Transceiver", "radio", "GFX\items\radio.b3d", "GFX\items\Icons\INV_radio.jpg", "GFX\items\radioHUD.png", 1.0)
	it = CreateItemTemplate("Radio Transceiver", "fineradio", "GFX\items\radio.b3d", "GFX\items\Icons\INV_radio.jpg", "GFX\items\radioHUD.png", 1.0)
	it = CreateItemTemplate("Radio Transceiver", "veryfineradio", "GFX\items\radio.b3d", "GFX\items\Icons\INV_radio.jpg", "GFX\items\radioHUD.png", 1.0)
	it = CreateItemTemplate("Radio Transceiver", "18vradio", "GFX\items\radio.b3d", "GFX\items\Icons\INV_radio.jpg", "GFX\items\radioHUD.png", 1.02)
	it = CreateItemTemplate("9V Battery", "bat", "GFX\items\Battery.x", "GFX\items\Icons\INV_battery_9v.jpg", "", 0.008)
	it = CreateItemTemplate("18V Battery", "18vbat", "GFX\items\Battery.x", "GFX\items\Icons\INV_battery_18v.jpg", "", 0.01, "GFX\items\Battery 18V.jpg")
	it = CreateItemTemplate("Strange Battery", "killbat", "GFX\items\Battery.x", "GFX\items\Icons\INV_battery_strange.jpg", "", 0.01,"GFX\items\Strange Battery.jpg")
	it = CreateItemTemplate("Playing Card", "misc", "GFX\items\key_card.x", "GFX\items\Icons\INV_card.jpg", "", 0.0004,"GFX\items\card.jpg")
	it = CreateItemTemplate("Mastercard", "misc", "GFX\items\key_card.x", "GFX\items\Icons\INV_master_card.jpg", "", 0.0004,"GFX\items\mastercard.jpg")
	it = CreateItemTemplate("Coin", "coin", "GFX\items\coin.b3d", "GFX\items\Icons\INV_coin_rust.jpg", "", 0.0005, "GFX\items\coinrust.png","",0,1+2+8) : it\sound = 3
	it = CreateItemTemplate("Quarter","25ct", "GFX\items\coin.b3d", "GFX\items\Icons\INV_coin.jpg", "", 0.0005, "GFX\items\coin.png","",0,1+2+8) : it\sound = 3
	it = CreateItemTemplate("Wallet","wallet", "GFX\items\wallet.b3d", "GFX\items\Icons\INV_wallet.jpg", "", 0.0005,"","",1) : it\sound = 2
	it = CreateItemTemplate("Red Key", "key", "GFX\items\key.b3d", "GFX\items\Icons\INV_red_key.jpg", "", 0.001, "GFX\items\red_key.png","",0,1+2+8) : it\sound = 3
	it = CreateItemTemplate("Green Key", "key", "GFX\items\key.b3d", "GFX\items\Icons\INV_green_key.jpg", "", 0.001, "GFX\items\green_key.png","",0,1+2+8) : it\sound = 3
	it = CreateItemTemplate("Blue Key", "key", "GFX\items\key.b3d", "GFX\items\Icons\INV_blue_key.jpg", "", 0.001, "GFX\items\blue_key.png","",0,1+2+8) : it\sound = 3
	
	; ~ [SCPS]
	
	it = CreateItemTemplate("Metal Panel", "scp148", "GFX\items\metalpanel.x", "GFX\items\Icons\INV_metal_panel.jpg", "", RoomScale) : it\sound = 2
	it = CreateItemTemplate("SCP-148 Ingot", "scp148ingot", "GFX\items\scp148.x", "GFX\items\Icons\INV_scp_148.jpg", "", RoomScale) : it\sound = 2
	it = CreateItemTemplate("Some SCP-420-J", "420", "GFX\items\420.x", "GFX\items\Icons\INV_scp_420.jpg", "", 0.0005) : it\sound = 2
	it = CreateItemTemplate("SCP-427","scp427","GFX\items\427.b3d","GFX\items\Icons\INV_scp_427.jpg", "", 0.001)
	it = CreateItemTemplate("SCP-500-01", "scp500pill", "GFX\items\scp_500_pill.b3d", "GFX\items\Icons\INV_scp_500_pill.jpg", "", 0.0001) : it\sound = 2
	it = CreateItemTemplate("SCP-513", "scp513", "GFX\items\513.x", "GFX\items\Icons\INV_scp_513.jpg", "", 0.1) : it\sound = 2
	it = CreateItemTemplate("SCP-714", "scp714", "GFX\items\scp714.b3d", "GFX\items\Icons\INV_scp_714.jpg", "", 0.3) : it\sound = 3
	it = CreateItemTemplate("SCP-860", "scp860", "GFX\items\scp_860.b3d", "GFX\items\Icons\INV_scp_860.png", "", 0.002) : it\sound = 3
	it = CreateItemTemplate("SCP-1025", "scp1025", "GFX\items\scp1025.b3d", "GFX\items\Icons\INV_scp_1025.jpg", "", 0.1) : it\sound = 0
	
	; ~ [WEARIBLE]
	
	it = CreateItemTemplate("Ballistic Vest", "vest", "GFX\items\Fine_vest.b3d", "GFX\items\Icons\INV_vest_fine.jpg", "", 0.022,"GFX\items\Fine_Vest.png") :it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate("Heavy Ballistic Vest", "finevest", "GFX\items\vest.x", "GFX\items\Icons\INV_vest.jpg", "", 0.02,"GFX\Npcs\Humans\Personnel\Guards\guard_diffuse.jpg") :it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate("Bulky Ballistic Vest", "veryfinevest", "GFX\items\Fine_vest.b3d", "GFX\items\Icons\INV_vest_fine.jpg", "", 0.025,"GFX\items\Heavy_Vest.png") :it\sound = 2
	it = CreateItemTemplate("Hazmat Suit", "hazmatsuit", "GFX\items\hazmat_suit.b3d", "GFX\items\Icons\INV_hazmat.jpg", "", 0.013,"","",1) : it\sound = 2
	it = CreateItemTemplate("Hazmat Suit", "hazmatsuit2", "GFX\items\hazmat_suit.b3d", "GFX\items\Icons\INV_hazmat.jpg", "", 0.013,"","",1) : it\sound = 2
	it = CreateItemTemplate("Heavy Hazmat Suit", "hazmatsuit3", "GFX\items\hazmat_suit.b3d", "GFX\items\Icons\INV_hazmat.jpg", "", 0.013,"","",1) : it\sound = 2
	it = CreateItemTemplate("Gas Mask", "gasmask", "GFX\items\gasmask.b3d", "GFX\items\Icons\INV_gas_mask.jpg", "", 0.02) : it\sound = 2
	it = CreateItemTemplate("Gas Mask", "supergasmask", "GFX\items\gasmask.b3d", "GFX\items\Icons\INV_gas_mask.jpg", "", 0.021) : it\sound = 2
	it = CreateItemTemplate("Heavy Gas Mask", "gasmask3", "GFX\items\gasmask.b3d", "GFX\items\Icons\INV_gas_mask.jpg", "", 0.021) : it\sound = 2
	it = CreateItemTemplate("Night Vision Goggles", "nvgoggles", "GFX\items\NVG.b3d", "GFX\items\Icons\INV_night_vision.jpg", "", 0.02,"GFX\items\Nvg_Green.png") : it\sound = 2
	it = CreateItemTemplate("Night Vision Goggles", "supernv", "GFX\items\NVG.b3d", "GFX\items\Icons\INV_night_vision_fine.jpg", "", 0.02,"GFX\items\Nvg_Blue.png") : it\sound = 2
	it = CreateItemTemplate("Night Vision Goggles", "finenvgoggles", "GFX\items\NVG.b3d", "GFX\items\Icons\INV_night_vision_very_fine.jpg", "", 0.02,"GFX\items\Nvg_Red.png") : it\sound = 2
	
	; ~ [USABLE]
	
	it = CreateItemTemplate("First Aid Kit", "firstaid", "GFX\items\firstaid.x", "GFX\items\Icons\INV_first_aid.png", "", 0.05)
	it = CreateItemTemplate("Small First Aid Kit", "finefirstaid", "GFX\items\small_firstaid.x", "GFX\items\Icons\INV_small_first_aid.jpg", "", 0.03,"GFX\items\small_firstaidkit.jpg")
	it = CreateItemTemplate("Blue First Aid Kit", "firstaid2", "GFX\items\firstaid.x", "GFX\items\Icons\INV_first_aid_2.png", "", 0.03, "GFX\items\firstaidkit2.jpg")
	it = CreateItemTemplate("Strange Bottle", "veryfinefirstaid", "GFX\items\eyedrops.b3d", "GFX\items\Icons\INV_bottle.jpg", "", 0.002, "GFX\items\bottle.jpg")	
	it = CreateItemTemplate("Syringe", "syringe", "GFX\items\syringe.b3d", "GFX\items\Icons\INV_Syringe.png", "", 0.005) : it\sound = 2
	it = CreateItemTemplate("Syringe", "finesyringe", "GFX\items\syringe.b3d", "GFX\items\Icons\INV_Syringe.png", "", 0.005) : it\sound = 2
	it = CreateItemTemplate("Syringe", "veryfinesyringe", "GFX\items\syringe.b3d", "GFX\items\Icons\INV_Syringe.png", "", 0.005) : it\sound = 2
	it = CreateItemTemplate("Eyedrops", "fineeyedrops", "GFX\items\eyedrops.b3d", "GFX\items\Icons\INV_eye_drops.jpg", "", 0.0012, "GFX\items\eyedrops.jpg")
	it = CreateItemTemplate("Eyedrops", "supereyedrops", "GFX\items\eyedrops.b3d", "GFX\items\Icons\INV_eye_drops.jpg", "", 0.0012, "GFX\items\eyedrops.jpg")
	it = CreateItemTemplate("ReVision Eyedrops", "eyedrops","GFX\items\eyedrops.b3d", "GFX\items\Icons\INV_eye_drops.jpg", "", 0.0012, "GFX\items\eyedrops.jpg")
	it = CreateItemTemplate("RedVision Eyedrops", "eyedrops", "GFX\items\eyedrops.b3d", "GFX\items\Icons\INV_eye_drops_red.jpg", "", 0.0012,"GFX\items\eyedropsred.jpg")
	it = CreateItemTemplate("Cigarette", "cigarette", "GFX\items\420.x", "GFX\items\Icons\INV_scp_420.jpg", "", 0.0004) : it\sound = 2
	it = CreateItemTemplate("Joint", "420s", "GFX\items\420.x", "GFX\items\Icons\INV_scp_420.jpg", "", 0.0004) : it\sound = 2
	it = CreateItemTemplate("Smelly Joint", "420s", "GFX\items\420.x", "GFX\items\Icons\INV_scp_420.jpg", "", 0.0004) : it\sound = 2
	it = CreateItemTemplate("Upgraded pill", "scp500death", "GFX\items\pill.b3d", "GFX\items\Icons\INV_scp_500_pill.jpg", "", 0.0001) : it\sound = 2 : EntityColor it\obj,255,0,0
	it = CreateItemTemplate("Pill", "pill", "GFX\items\pill.b3d", "GFX\items\Icons\INV_scp_500_pill_white.jpg", "", 0.0001) : it\sound = 2 : EntityColor it\obj,255,255,255
	
	; ----------------------
	
	;! --- NINE-TAILED FOX ---
	
	; ~ [PAPER]
	
	it = CreateItemTemplate("Document SCP-109", "paper", "GFX\items\paper.x", "GFX\items\Icons\INV_paper.jpg", "GFX\items\doc109.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-457 Page 1/2","paper","GFX\items\paper.x","GFX\items\Icons\INV_paper.jpg","GFX\items\doc457_1.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Document SCP-457 Page 2/2","paper","GFX\items\paper.x","GFX\items\Icons\INV_paper.jpg","GFX\items\doc457_2.jpg", 0.003) : it\sound = 0
	
	; ~ [SCPS]
	
	it = CreateItemTemplate("SCP-109","scp109","GFX\items\scp_109.b3d","GFX\items\Icons\INV_scp_109.jpg","",0.0009,"","",1) : it\sound = 4
	it = CreateItemTemplate("SCP-198","scp198","GFX\items\scp_198.b3d","GFX\items\Icons\INV_scp_198.jpg","",0.04)
	
	; ~ [MISC]
	
	it = CreateItemTemplate("Fuse", "fuse", "GFX\items\fuse.b3d", "GFX\items\Icons\INV_fuse.jpg", "", 0.025, "GFX\map\textures\fuse.png")
	
	; ----------------------
	
	For it = Each ItemTemplates
		If (it\tex<>0) Then
			If (it\texpath<>"") Then
				For it2=Each ItemTemplates
					If (it2<>it) And (it2\tex=it\tex) Then
						it2\tex = 0
					EndIf
				Next
			EndIf
			DeleteSingleTextureEntryFromCache it\tex : it\tex = 0
		EndIf
	Next
	
End Function 

Type Items
	Field name$
	Field collider%,model%
	Field itemtemplate.ItemTemplates
	Field DropSpeed#
	Field r%,g%,b%,a#
	Field SoundChn%
	Field dist#, disttimer#
	Field state#, state2#, state3#, state4#, state5#
	Field Picked%,Dropped%
	Field invimg%
	Field WontColl% = False
	Field xspeed#,zspeed#
	Field SecondInv.Items[20]
	Field ID%
	Field invSlots%
	Field noDelete%
	Field OverHereSprite%
End Type 

Function CreateItem.Items(name$, tempname$, x#, y#, z#, r%=0,g%=0,b%=0,a#=1.0,invSlots%=0)
	CatchErrors("Uncaught (CreateItem)")
	
	Local i.Items = New Items
	Local it.ItemTemplates
	
	name = Lower(name)
	tempname = Lower (tempname)
	
	For it.ItemTemplates = Each ItemTemplates
		If Lower(it\name) = name Then
			If Lower(it\tempname) = tempname Then
				i\itemtemplate = it
				i\collider = CreatePivot()
				EntityRadius i\collider, 0.01
				EntityPickMode i\collider, 1, False
				i\model = CopyEntity(it\obj,i\collider)
				i\name = it\name
				ShowEntity i\collider
				ShowEntity i\model
			EndIf
		EndIf
	Next 
	
	i\WontColl = False
	
	If i\itemtemplate = Null Then RuntimeError("Item template not found ("+name+", "+tempname+")")
	
	ResetEntity i\collider		
	PositionEntity(i\collider, x, y, z, True)
	RotateEntity (i\collider, 0, Rand(360), 0)
	If gc\CurrGamemode<>3 Then
		i\dist = EntityDistance(Collider, i\collider)
	Else
		i\dist = EntityDistance(Players[mp_I\PlayerID]\Collider, i\collider) ;TODO item distance squared?
	EndIf
	i\DropSpeed = 0.0
	
	If tempname = "cup" Then
		i\r=r
		i\g=g
		i\b=b
		i\a=a
		
		Local liquid = CopyEntity(LiquidObj)
		ScaleEntity liquid, i\itemtemplate\scale,i\itemtemplate\scale,i\itemtemplate\scale,True
		PositionEntity liquid, EntityX(i\collider,True),EntityY(i\collider,True),EntityZ(i\collider,True)
		EntityParent liquid, i\model
		EntityColor liquid, r,g,b
		
		If a < 0 Then 
			EntityFX liquid, 1
			EntityAlpha liquid, Abs(a)
		Else
			EntityAlpha liquid, Abs(a)
		EndIf
		
		
		EntityShininess liquid, 1.0
	EndIf
	
	i\invimg = i\itemtemplate\invimg
	If (tempname="clipboard") And (invSlots=0) Then
		invSlots = 10
		SetAnimTime i\model,17.0
		i\invimg = i\itemtemplate\invimg2
	EndIf
	If (tempname="wallet") And (invSlots=0) Then
		invSlots = 10
	EndIf
	If tempname="hazmatsuit" Lor tempname="hazmatsuit2" Lor tempname="hazmatsuit3" Then
		SetAnimTime i\model,2.0
	EndIf
	
	i\invSlots=invSlots
	
	i\ID=LastItemID+1
	LastItemID=i\ID
	
	CatchErrors("CreateItem")
	Return i
End Function

Function RemoveItem(i.Items)
	CatchErrors("Uncaught (RemoveItem)")
	Local n
	FreeEntity(i\model) : FreeEntity(i\collider) : i\collider = 0
	
	For n% = 0 To MaxItemAmount - 1
		If Inventory[n] = i
			DebugLog "Removed "+i\itemtemplate\name+" from slot "+n
			Inventory[n] = Null
			ItemAmount = ItemAmount-1
			Exit
		EndIf
	Next
	If SelectedItem = i Then
		Select SelectedItem\itemtemplate\tempname
			Case "scramble", "veryfinescramble"
				wbi\SCRAMBLE = False
			Case "nvgoggles", "supernv", "finenvgoggles"
				wbi\NightVision = False
			Case "gasmask", "supergasmask", "gasmask2", "gasmask3"
				wbi\GasMask = False
			Case "vest","finevest", "veryfinevest"
				wbi\Vest = False
			Case "hazmatsuit","hazmatsuit2","hazmatsuit3"
				wbi\Hazmat = False	
			Case "helmet", "finehelmet"
				wbi\Helmet = False
				psp\Helmet = 0
			Case "scp714"
				I_714\Using = 0
			Case "scp268","super268"
				I_268\Using = False
			Case "scp1033ru", "super1033ru"
			    I_1033RU\Using = False
			Case "scp059"
				I_059\Timer = 0.9
				I_059\Using = False
			Case "scp357"
				I_357\Timer = 0.0
				I_357\Using = False
			Case "scp402"
				I_402\Using = False
		End Select
		
		SelectedItem = Null
	EndIf
	If i\itemtemplate\img <> 0
		FreeImage i\itemtemplate\img
		i\itemtemplate\img = 0
	EndIf
	i\OverHereSprite = FreeEntity_Strict(i\OverHereSprite)
	Delete i
	
	CatchErrors("RemoveItem")
End Function

Function UpdateItems()
	CatchErrors("Uncaught (UpdateItems)")
	Local n, i.Items, i2.Items
	Local xtemp#, ytemp#, ztemp#
	Local temp%, np.NPCs
	Local pick%
	
	Local HideDist = HideDistance*0.5
	Local deletedItem% = False
	
	ClosestItem = Null
	For i.Items = Each Items
		i\Dropped = 0
		
		If (Not i\Picked) Then
			If i\disttimer < MilliSecs() Then
				i\dist = EntityDistance(Camera, i\collider)
				i\disttimer = MilliSecs() + 700
				If i\dist < HideDist Then ShowEntity i\collider
			EndIf
			
			If i\dist < HideDist Then
				ShowEntity i\collider
				
				If i\dist < 1.2 Then
					If ClosestItem = Null Then
						If EntityInView(i\model, Camera) Then
							If EntityVisible(i\collider,Camera) Then
								ClosestItem = i
							EndIf
						EndIf
					ElseIf ClosestItem = i Lor i\dist < EntityDistance(Camera, ClosestItem\collider) Then 
						If EntityInView(i\model, Camera) Then
							If EntityVisible(i\collider,Camera) Then
								ClosestItem = i
							EndIf
						EndIf
					EndIf
				EndIf
				
				If EntityCollided(i\collider, HIT_MAP) Then
					i\DropSpeed = 0
					i\xspeed = 0.0
					i\zspeed = 0.0
				Else
					If ShouldEntitiesFall
						pick = LinePick(EntityX(i\collider),EntityY(i\collider),EntityZ(i\collider),0,-10,0)
						If pick
							i\DropSpeed = i\DropSpeed - 0.0004 * fps\Factor[0]
							TranslateEntity i\collider, i\xspeed*fps\Factor[0], i\DropSpeed * fps\Factor[0], i\zspeed*fps\Factor[0]
							If i\WontColl Then ResetEntity(i\collider)
						Else
							i\DropSpeed = 0
							i\xspeed = 0.0
							i\zspeed = 0.0
						EndIf
					Else
						i\DropSpeed = 0
						i\xspeed = 0.0
						i\zspeed = 0.0
					EndIf
				EndIf
				
				If i\dist<HideDist*0.2 Then
					For i2.Items = Each Items
						If i<>i2 And (Not i2\Picked) And i2\dist<HideDist*0.2 Then
							
							xtemp# = (EntityX(i2\collider,True)-EntityX(i\collider,True))
							ytemp# = (EntityY(i2\collider,True)-EntityY(i\collider,True))
							ztemp# = (EntityZ(i2\collider,True)-EntityZ(i\collider,True))
							
							Local ed# = (xtemp*xtemp+ztemp*ztemp)
							If ed<0.07 And Abs(ytemp)<0.25 Then
								;items are too close together, push away
								If PlayerRoom\RoomTemplate\Name	<> "cont_970" Then
									xtemp = xtemp*(0.06-ed)
									ztemp = ztemp*(0.06-ed)
									
									While Abs(xtemp)+Abs(ztemp)<0.001
										xtemp = xtemp+Rnd(-0.001,0.001)
										ztemp = ztemp+Rnd(-0.001,0.001)
									Wend
									
									TranslateEntity i2\collider,xtemp,0,ztemp
									TranslateEntity i\collider,-xtemp,0,-ztemp
								EndIf
							EndIf
						EndIf
					Next
				EndIf
				
				If EntityY(i\collider) < - 35.0 Then DebugLog "remove: " + i\itemtemplate\name:RemoveItem(i):deletedItem=True
			Else
				HideEntity i\collider
			EndIf
		Else
			i\DropSpeed = 0
			i\xspeed = 0.0
			i\zspeed = 0.0
		EndIf
		
		If Not deletedItem Then
			CatchErrors(Chr(34)+i\itemtemplate\name+Chr(34)+" item")
		EndIf
		deletedItem = False
	Next
	
	If ClosestItem <> Null And I_330\Taken < 3 Then
		If KeyHitUse Then PickItem(ClosestItem)
	EndIf
	
End Function

Function PickItem(item.Items)
	Local n% = 0, z%
	Local g.Guns, e.Events
	
	CatchErrors("Uncaught (PickItem)")
	If (Not item\itemtemplate\isGun) And (Not item\itemtemplate\fastPickable)
		If ItemAmount < MaxItemAmount Then
			For n% = 0 To MaxItemAmount - 1
				If Inventory[n] = Null Then
					Select item\itemtemplate\tempname
						Case "scp109","scp178"
							SetAnimTime item\model, 19.0
						Case "hazmatsuit", "hazmatsuit2", "hazmatsuit3"
							SetAnimTime item\model,5.0
						Case "killbat"
							LightFlash = 1.0
							PlaySound_Strict(IntroSFX[2])
							If I_1033RU\HP = 0
								m_msg\DeathTxt = GetLocalStringR("Singleplayer","killbad_death_1",Designation)
								m_msg\DeathTxt = m_msg\DeathTxt + GetLocalString("Singleplayer","killbad_death_2")
								Kill()
							Else
								Damage1033RU(100 * I_1033RU\Using)
							EndIf
						Case "scp148"
							GiveAchievement(Achv148)	
						Case "scp513"
							GiveAchievement(Achv513)
						Case "scp860"
							GiveAchievement(Achv860)
						Case "scp207"
							If I_402\Timer > 0 Then
								PlaySound_Strict(HorrorSFX[Rand(0, 3)])
								CreateMsg(Chr(34) + GetLocalString("Singleplayer","i_cant") + Chr(34))
								Exit
							Else
								GiveAchievement(Achv207)
							EndIf
						Case "scp207empty"
							CreateMsg(GetLocalString("Items", "scp207_empty"))
							Exit
						Case "scp357"
							;[Block]
							If (Not I_059\Using) Then
								I_357\Using = True
								GiveAchievement(Achv357)
								CreateMsg(GetLocalString("Items", "scp357_2"))
								I_357\Timer = 1.0
							Else
								CreateMsg(GetLocalString("Items", "scp357_3"))
								Exit
							EndIf
							;[End Block]
						Case "scp059"
							If (Not I_357\Using) Then
								I_059\Using = True
								GiveAchievement(Achv059)
								CreateMsg(GetLocalString("Items", "scp059_1"))
								I_059\Timer = 10.0
							Else
								CreateMsg(GetLocalString("Items", "scp059_2"))
								Exit
							EndIf
						Case "scp198"
							;[Block]
							GiveAchievement(Achv198)
							I_198\Timer = 0
							CreateMsg(GetLocalString("Items", "scp198_schock"))
							PlaySound_Strict LoadTempSound("SFX\SCP\198\Shock.ogg")
							LightFlash = 2.5
							BlurTimer = 1000
							Sanity = Max(-850, Sanity)
							I_198\Timer = 1
							If I_1033RU\HP = 0
								DamageSPPlayer(Rand(10,15),True)
							Else
								Damage1033RU(30 + (5 * SelectedDifficulty\AggressiveNPCs))
							EndIf
							;[End Block]
						Case "scp035"
							;[Block]
							
							Local snd.Sound,i
							
							If (Not I_714\Using Lor wbi\GasMask Lor wbi\Hazmat Lor mpl\HasNTFGasmask) Then
								
								RemoveItem(item)
								
								For snd = Each Sound
									For i = 0 To 31
										If snd\channels[i]<>0 Then
											StopChannel snd\channels[i]
										EndIf
									Next
								Next
								
								For g.Guns = Each Guns
									If g\ID = g_I\HoldingGun Then
										PlayGunSound(g\name$+"\holster",1,1,False)
									EndIf
								Next
								g_I\GunChangeFLAG = False
								g_I\HoldingGun = 0
								g_I\Weapon_CurrSlot = 0
								mpl\SlotsDisplayTimer = 0
								
								I_035\Possessed = True
								
								InvOpen = False
								GiveAchievement(Achv035)
								PlaySound_Strict LoadTempSound("SFX\SCP\035\Shock.ogg")
								LightFlash = 2
								BlurTimer = 100
								If KillTimer >= 0 And FallTimer >= 0 Then
									FallTimer = Min(-1, FallTimer)
									PositionEntity(Head, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True), True)
									ResetEntity (Head)
									RotateEntity(Head, 0, EntityYaw(Camera) + Rand(-45, 45), 0)
									mpl\DamageTimer = 70.0*1.0
									If wbi\Vest Then
										psp\Kevlar = 0
									EndIf
									If hds\Wearing Then
										hds\Wearing = False
									EndIf
									psp\Health = 1
								EndIf
								If gc\CurrZone = LCZ Lor gc\CurrZone = HCZ Lor gc\CurrZone = EZ Then
									Local r.Rooms
									For r = Each Rooms
										If r\RoomTemplate\Name = "area_035_ntf_encounter" Then
											PlayerRoom = r
											TeleportEntity(Collider, r\x,r\y,r\z)
											Exit
										EndIf
									Next
									DropSpeed = 0
									FallTimer = 0
									psp\NoMove = False
									psp\NoRotation = False
									IsZombie = False
									FlushKeys()
									FlushMouse()
									FlushJoy()
									Return
								Else
									Kill()
								EndIf
							Else
								Exit
							EndIf
							;[End Block]
						Case "scp1102ru"
							;[Block]
							item\state = 0
							;[End Block]
						Case "key6"
							GiveAchievement(AchvOmni)
						Case "veryfinevest"
							CreateMsg(GetLocalString("Items","vest_heavy"))
							Exit
						Case "hds_suit"
							item\state = 0
						Case "firstaid", "finefirstaid", "veryfinefirstaid", "firstaid2"
							item\state = 0
							If I_402\Timer > 0 Then
								PlaySound_Strict(HorrorSFX[Rand(0, 3)])
								CreateMsg(Chr(34) + GetLocalString("Singleplayer","i_cant") + Chr(34))
								Exit
							EndIf
						Case "navigator", "nav"
							If item\itemtemplate\name = "S-NAV Navigator Ultimate" Then GiveAchievement(AchvSNAV)
						Case "paper"
							If item\itemtemplate\name = "Infected SCP-409 Document" Then
								CreateMsg(GetLocalString("Items","scp409_9"))
								I_409\Timer = 1
							EndIf
						Case "brokenhelmet"
							CreateMsg(GetLocalString("Items","brokenhelmet"))
							Exit
					End Select
					
					CreateMsg(item\itemtemplate\name + " " + GetLocalString("Items","item_picked_up"))
					
					If item\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[item\itemtemplate\sound])
					item\Picked = True
					item\Dropped = -1
					
					item\itemtemplate\found=True
					ItemAmount = ItemAmount + 1
					
					Inventory[n] = item
					HideEntity(item\collider)
					Exit
				EndIf
			Next
		Else
			CreateMsg(GetLocalString("Items","cannot_carry"))
		EndIf
	ElseIf (item\itemtemplate\isGun) And (Not item\itemtemplate\fastPickable)
		
		Local found = False
		For n = 0 To MaxItemAmount-1
			If Inventory[n] <> Null And Inventory[n]\itemtemplate\tempname = item\itemtemplate\tempname Then
				found = True
				Exit
			EndIf
		Next
		For g = Each Guns
			If g\name = item\itemtemplate\tempname Then
				If g\GunType = GUNTYPE_MELEE And found Then
					CreateMsg(GetLocalString("Items","already_have")+item\itemtemplate\name)
					Exit
				EndIf
				If (Not found) Lor (g\GunType = GUNTYPE_GRENADE) Then
					If ItemAmount < MaxItemAmount Then
						For n = 0 To MaxItemAmount - 1
							If Inventory[n] = Null Then
								PlaySound_Strict LoadTempSound("SFX\Guns\" + item\itemtemplate\tempname + "\pickup.ogg")
								item\Picked = True
								item\Dropped = -1
								item\itemtemplate\found = True
								Inventory[n] = item
								HideEntity item\collider
								g\CurrAmmo = item\state
								g\CurrReloadAmmo = item\state2
								Exit
							EndIf
						Next
					Else
						CreateMsg(GetLocalString("Items","cannot_carry"))
					EndIf
				Else
					If g\CurrReloadAmmo < g\MaxReloadAmmo Then
						If g\GunType = GUNTYPE_SHOTGUN Then
							Local prev = g\CurrReloadAmmo
							g\CurrReloadAmmo = Min(g\CurrReloadAmmo+item\state+item\state2,g\MaxReloadAmmo)
							CreateMsg((g\CurrReloadAmmo - prev) + " " + item\itemtemplate\name + " " +GetLocalString("Weapons","shells_picked_up"))
						Else
							g\CurrReloadAmmo = Min(g\CurrReloadAmmo+item\state+item\state2,g\MaxReloadAmmo)
							If item\state > 0 Then
								CreateMsg(GetLocalString("Weapons","ammo_picked_up_1")+" "+item\itemtemplate\name+" "+GetLocalString("Weapons","ammo_picked_up_2"))
							Else
								CreateMsg(GetLocalString("Weapons","no_ammo_1")+" "+item\itemtemplate\name+GetLocalString("Weapons","no_ammo_2"))
							EndIf
						EndIf
						PlaySound_Strict LoadTempSound("SFX\Guns\" + item\itemtemplate\tempname + "\pickup.ogg")
						RemoveItem(item)
					Else
						CreateMsg(GetLocalString("Weapons","cant_have_ammo") +" "+item\itemtemplate\name)
					EndIf
				EndIf
				Exit
			EndIf
		Next
	Else
		Select item\itemtemplate\tempname
			Case "vest"
				If (Not hds\Wearing) Then
					If (Not wbi\Vest) Lor psp\Kevlar < 100 Then
						CreateMsg(GetLocalString("Items","vest_on"))
						PlaySound_Strict LoadTempSound("SFX\Interact\PickUpKevlar.ogg")
						psp\Kevlar = 100
						If (Not wbi\Vest) Then
							wbi\Vest = 1
						EndIf
						RemoveItem(item)
					Else
						CreateMsg(GetLocalString("Items","cant_replace_vest"))
					EndIf
				Else
					CreateMsg(GetLocalString("Items","cant_wear_vest_with_hds"))
				EndIf
			Case "finevest"
				If (Not hds\Wearing) Then
					If (Not wbi\Vest) Lor psp\Kevlar < 150 Then
						CreateMsg(GetLocalString("Items","vest_on"))
						PlaySound_Strict LoadTempSound("SFX\Interact\PickUpKevlar.ogg")
						psp\Kevlar = 150
						If (Not wbi\Vest) Then
							wbi\Vest = 2
						EndIf
						RemoveItem(item)
					Else
						CreateMsg(GetLocalString("Items","cant_replace_vest"))
					EndIf
				Else
					CreateMsg(GetLocalString("Items","cant_wear_vest_with_hds"))
				EndIf
			Case "hds_suit"
				If (Not hds\Wearing) Then
					PlaySound_Strict LoadTempSound("SFX\Interact\PickUpKevlar.ogg")
					CreateMsg(GetLocalString("Items","hds_on"))
					hds\Wearing = True
					
					hds\Health = 100
					
					wbi\GasMask = 0
					wbi\Hazmat = 0
					wbi\Helmet = 0
					wbi\Vest = 0
					wbi\SCRAMBLE = 0
					wbi\NightVision = 0
					mpl\HasNTFGasmask = 0
					RemoveItem(item)
				EndIf
			Case "hds_fuse"
				;[Block]
				If hds\Wearing Then
					If hds\Health <> 100 Then
						hds\Health = Min(hds\Health + 15, 100.0)
						If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[23])
						RemoveItem(item)
					Else
						CreateMsg(GetLocalString("Items","enough_power"))
					EndIf
				Else
					CreateMsg(GetLocalString("Items","need_hds"))
				EndIf
				;[End Block]
			Case "aimpoint"
				CreateMsg(GetLocalString("Weapons","picked_aimpoint"))
				RemoveItem(item)
				For g.Guns = Each Guns
					g\PickedUpAimPoint = True
				Next
			Case "eotech"
				CreateMsg(GetLocalString("Weapons","picked_eotech"))
				RemoveItem(item)
				For g.Guns = Each Guns
					g\PickedUpEotech = True
				Next
			Case "verticalgrip"
				CreateMsg(GetLocalString("Weapons","picked_grip"))
				RemoveItem(item)
				For g.Guns = Each Guns
					g\PickedUpVerticalGrip = True
				Next
			Case "suppressor"
				CreateMsg(GetLocalString("Weapons","picked_suppressor"))
				RemoveItem(item)
				For g.Guns = Each Guns
					g\PickedUpSuppressor = True
				Next
			Case "reddot"
				CreateMsg(GetLocalString("Weapons","picked_reddot"))
				RemoveItem(item)
				For g.Guns = Each Guns
					g\PickedUpRedDot = True
				Next
			Case "acog"
				CreateMsg(GetLocalString("Weapons","picked_acog"))
				RemoveItem(item)
				For g.Guns = Each Guns
					g\PickedUpAcog = True
				Next
			Case "mp5stock"
				CreateMsg(GetLocalString("Weapons","picked_stock_1"))
				RemoveItem(item)
				For g.Guns = Each Guns
					g\PickedUpStock = True
				Next
			Case "mp5stock2"
				CreateMsg(GetLocalString("Weapons","picked_stock_2"))
				RemoveItem(item)
				For g.Guns = Each Guns
					g\PickedUpFoldingStock = True
				Next
			Case "lasersight"
				CreateMsg(GetLocalString("Weapons","picked_laser"))
				RemoveItem(item)
				For g.Guns = Each Guns
					g\PickedUpLaserSight = True
				Next
			Case "extmag"
				CreateMsg(GetLocalString("Weapons","picked_extmag"))
				RemoveItem(item)
				For g.Guns = Each Guns
					g\PickedUpExtMag = True
				Next
			Case "mui"
				CreateMsg(GetLocalString("Weapons","picked_mui"))
				RemoveItem(item)
				For g.Guns = Each Guns
					g\PickedUpMUI = True
				Next
		End Select
	EndIf
	CatchErrors("PickItem")
End Function

Function DropItem(item.Items)
	CatchErrors("Uncaught (DropItem)")
	Local g.Guns,z
	
	If item\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[item\itemtemplate\sound])
	
	If item\itemtemplate\isGun Then
		For g.Guns = Each Guns
			If g\name$ = item\itemtemplate\tempname
				item\state = g\CurrAmmo
				item\state2 = g\CurrReloadAmmo
				If g\ID% <> g_I\HoldingGun Then
					PlayGunSound(g\name$+"\holster",1,1,False)
					Exit
				EndIf
			EndIf
		Next
	EndIf
	
	item\Dropped = 1
	
	ShowEntity(item\collider)
	PositionEntity(item\collider, EntityX(Camera), EntityY(Camera), EntityZ(Camera))
	RotateEntity(item\collider, EntityPitch(Camera), EntityYaw(Camera)+Rnd(-20,20), 0)
	MoveEntity(item\collider, 0, -0.1, 0.1)
	RotateEntity(item\collider, 0, EntityYaw(Camera)+Rnd(-110,110), 0)
	
	ResetEntity (item\collider)
	
	item\Picked = False
	For z% = 0 To MaxItemAmount - 1
		If Inventory[z] = item Then Inventory[z] = Null
	Next
	Select item\itemtemplate\tempname
		Case "gasmask", "supergasmask", "gasmask3"
			wbi\GasMask = False
		Case "hazmatsuit",  "hazmatsuit2", "hazmatsuit3"
			wbi\Hazmat = False
		Case "helmet", "finehelmet"
			wbi\Helmet = False
			psp\Helmet = 0
		Case "vest","finevest"
			wbi\Vest = False
		Case "scramble", "veryfinescramble"
			wbi\SCRAMBLE = False
		Case "nvgoggles"
			If wbi\NightVision = 1 Then CameraFogFar = StoredCameraFogFar : wbi\NightVision = False
		Case "supernv"
			If wbi\NightVision = 2 Then CameraFogFar = StoredCameraFogFar : wbi\NightVision = False
		Case "finenvgoggles"
			If wbi\NightVision = 3 Then CameraFogFar = StoredCameraFogFar : wbi\NightVision = False
		Case "scp714"
			I_714\Using = 0
		Case "scp268","super268"
			I_268\Using = False
		Case "scp1033ru", "super1033ru"
			I_1033RU\Using = False
		Case "scp059"
			I_059\Timer = 5.0
			I_059\Using = False
		Case "scp357"
			I_357\Timer = 0.0
			I_357\Using = False
		Case "scp402"
			I_402\Using = False
	End Select
	
	CreateMsg(item\itemtemplate\name  + " " +  GetLocalString("Items","item_dropped"))
	
	CatchErrors("DropItem")
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D