Function UpdateGUI()
	CatchErrors("Uncaught (UpdateGUI)")
	
	Local temp%, x%, y%, z%, i%, yawvalue#, pitchvalue#
	Local x2#,y2#,z2#
	Local n%, xtemp, ytemp, StrTemp$,d.Doors
	
	Local e.Events, it.Items
	
	If d_I\ClosestButton <> 0 And I_330\Taken < 3 And d_I\SelectedDoor = Null And InvOpen = False And MenuOpen = False And OtherOpen = Null And (Not PlayerInNewElevator)
		temp% = CreatePivot()
		PositionEntity temp, EntityX(Camera), EntityY(Camera), EntityZ(Camera)
		PointEntity temp, d_I\ClosestButton
		yawvalue# = WrapAngle(EntityYaw(Camera) - EntityYaw(temp))
		If yawvalue > 90 And yawvalue <= 180 Then yawvalue = 90
		If yawvalue > 180 And yawvalue < 270 Then yawvalue = 270
		pitchvalue# = WrapAngle(EntityPitch(Camera) - EntityPitch(temp))
		If pitchvalue > 90 And pitchvalue <= 180 Then pitchvalue = 90
		If pitchvalue > 180 And pitchvalue < 270 Then pitchvalue = 270
		
		FreeEntity (temp)
		
		If HUDenabled And psp\IsShowingHUD Then
			If d_I\ClosestButton <> 0 And d_I\ClosestDoor = Null Then
				DrawImage(PlayerIcons[3], opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32)
			EndIf
			
			If d_I\ClosestDoor <> Null Then
				DrawImage(PlayerIcons[3], opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32)
			EndIf
		EndIf
		
		Local ne.NewElevator
		
		If KeyHitUse
			If d_I\ClosestDoor <> Null Then 
				If d_I\ClosestDoor\Code <> "" Lor (d_I\ClosestDoor\dir=DOOR_ELEVATOR_3FLOOR And d_I\ClosestButton=d_I\ClosestDoor\buttons[0]) Then
					d_I\SelectedDoor = d_I\ClosestDoor
					co\KeyPad_CurrButton = 0
					co\WaitTimer = 0
				ElseIf d_I\ClosestDoor\dir=DOOR_ELEVATOR_3FLOOR And d_I\ClosestButton=d_I\ClosestDoor\buttons[1] Then
					PlaySound2(ButtonSFX[0], Camera, d_I\ClosestButton)
					For ne = Each NewElevator
						If ne\door = d_I\ClosestDoor
							If ne\state = 0.0
								If EntityY(ne\door\frameobj)>ne\floory[1]*RoomScale+1
									StartNewElevator(d_I\ClosestDoor,3)
									DebugLog "Option 3"
								ElseIf EntityY(ne\door\frameobj)<ne\floory[2]*RoomScale-1 And EntityY(ne\door\frameobj)>ne\floory[0]*RoomScale
									StartNewElevator(d_I\ClosestDoor,2)
									DebugLog "Option 2"
								Else
									StartNewElevator(d_I\ClosestDoor,1)
									DebugLog "Option 1"
								EndIf
							Else
								If (m_msg\Txt<>GetLocalString("Doors", "elevator_called"))
									If (CreateMsg(GetLocalString("Doors", "elevator_called2"))) Lor (m_msg\Timer<70*3)	
										Select Rand(10)
											Case 1
												CreateMsg(GetLocalString("Doors", "elevator_rand_1"))
											Case 2
												CreateMsg(GetLocalString("Doors", "elevator_rand_2"))
											Case 3
												CreateMsg(GetLocalString("Doors", "elevator_rand_3"))
											Default
												CreateMsg(GetLocalString("Doors", "elevator_called2"))
										End Select
									EndIf
								Else
									CreateMsg(GetLocalString("Doors", "elevator_called2"))
								EndIf
							EndIf
						EndIf
					Next
				ElseIf Playable Then
					PlaySound2(ButtonSFX[0], Camera, d_I\ClosestButton)
					UseDoor(d_I\ClosestDoor,True)
				EndIf
			EndIf
		EndIf
	EndIf
	
	If (Not MenuOpen) And (Not InvOpen) And (OtherOpen=Null) And (ConsoleOpen=False) And (Using294=False) And (SelectedScreen=Null) And EndingTimer=>0 And KillTimer >= 0
		If PlayerRoom\RoomTemplate\Name$ <> "room1_intro"
		EndIf
		
		UpdateCommunicationAndSocialWheel()
	EndIf
	
	UpdateSplashTexts()
	UpdateSplashMsg()
	
	If (Not MenuOpen) And (Not AttachmentOpen) And (Not InvOpen) And (OtherOpen=Null) And (d_I\SelectedDoor=Null) And (ConsoleOpen=False) And (Using294=False) And (SelectedScreen=Null) And EndingTimer=>0 And KillTimer >= 0
		ToggleGuns()
	EndIf
	mpl\SlotsDisplayTimer = Max(mpl\SlotsDisplayTimer-fps\Factor[0],0.0)
	
	If SelectedScreen <> Null Then
		If MouseUp1 Lor MouseHit2 Then
			FreeImage SelectedScreen\img : SelectedScreen\img = 0
			SelectedScreen = Null
			MouseUp1 = False
		EndIf
	EndIf
	
	Local PrevInvOpen% = InvOpen, MouseSlot% = 66
	
	Local PrevAttachmentOpen% = AttachmentOpen
	
	Local g.Guns
	
	Local shouldDrawHUD%=True
	If d_I\SelectedDoor <> Null Then
		If SelectedItem <> Null Then
			If SelectedItem\itemtemplate\tempname = "scp005" Then
				UseDoor(d_I\SelectedDoor)
				shouldDrawHUD = False
			Else
				SelectedItem = Null
			EndIf
		EndIf
		
		If shouldDrawHUD Then
			HideEntity g_I\GunPivot
			If d_I\SelectedDoor\dir<>5 Then
				Local pvt = CreatePivot()
				PositionEntity pvt, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True),EntityZ(d_I\ClosestButton,True)
				RotateEntity pvt, 0, EntityYaw(d_I\ClosestButton,True)-180,0
				MoveEntity pvt, 0,0,0.22
				PositionEntity Camera, EntityX(pvt),EntityY(pvt),EntityZ(pvt)
				PointEntity Camera, d_I\ClosestButton
				FreeEntity pvt	
				
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)+MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				Local projY# = ProjectedY()
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)-MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				Local scale# = (ProjectedY()-projY)/462.0
				
				x = opt\GraphicWidth/2-317*scale/2
				y = opt\GraphicHeight/2-462*scale/2
				
				If KeypadMSG <> "" Then 
					KeypadTimer = KeypadTimer-fps\Factor[1]
					
					If KeypadTimer =<0 Then
						KeypadMSG = ""
						d_I\SelectedDoor = Null
						MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
					EndIf
				EndIf
				
				x = x+44*scale
				y = y+249*scale
				
				For n = 0 To 3
					For i = 0 To 2
						xtemp = x+Int(58.5*scale*n)
						ytemp = y+(67*scale)*i
						
						temp = False
						If MouseOn(xtemp,ytemp, 54*scale,65*scale) And KeypadMSG = "" Then
							If MouseUp1 Then 
								PlaySound_Strict ButtonSFX[0]
								
								Select (n+1)+(i*4)
									Case 1,2,3
										KeypadInput=KeypadInput + ((n+1)+(i*4))
									Case 4
										KeypadInput=KeypadInput + "0"
									Case 5,6,7
										KeypadInput=KeypadInput + ((n+1)+(i*4)-1)
									Case 8
										If KeypadInput = d_I\SelectedDoor\Code Then
											PlaySound_Strict ScannerSFX1
											
											If d_I\SelectedDoor\Code = Str(AccessCode[0]) Then
												GiveAchievement(AchvMaynard)
											ElseIf d_I\SelectedDoor\Code = "7816"
												GiveAchievement(AchvHarp)
											EndIf									
											
											d_I\SelectedDoor\locked = 0
											UseDoor(d_I\SelectedDoor,True)
											d_I\SelectedDoor = Null
											MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
										Else
											PlaySound_Strict ScannerSFX2
											KeypadMSG = Upper(GetLocalString("Doors", "keypad_denied"))
											KeypadTimer = 210
											KeypadInput = ""	
										EndIf
									Case 9,10,11
										KeypadInput=KeypadInput + ((n+1)+(i*4)-2)
									Case 12
										KeypadInput = ""
								End Select 
								
								If Len(KeypadInput)> 4 Then KeypadInput = Left(KeypadInput,4)
							EndIf
							
						Else
							temp = False
						EndIf
						
					Next
				Next
			Else
				pvt = CreatePivot()
				PositionEntity pvt, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True),EntityZ(d_I\ClosestButton,True)
				RotateEntity pvt, 0, EntityYaw(d_I\ClosestButton,True)-180,0
				MoveEntity pvt, 0,0,0.3
				PositionEntity Camera, EntityX(pvt),EntityY(pvt),EntityZ(pvt)
				PointEntity Camera, d_I\ClosestButton
				FreeEntity pvt
				
				CameraZoom Camera,1.0
				
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)+MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				projY# = ProjectedY()
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)-MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				scale# = (ProjectedY()-projY)/462.0
				
				MoveEntity Camera,0.001,0.1672,0
				
				x = opt\GraphicWidth/2-317*scale/2
				y = opt\GraphicHeight/2-462*scale/2
				
				If co\Enabled
					If co\WaitTimer = 0.0
						If GetDPadButtonPress()=0
							co\KeyPad_CurrButton = co\KeyPad_CurrButton - 1
							PlaySound_Strict co\SelectSFX
							co\WaitTimer = fps\Factor[1]
							If co\KeyPad_CurrButton < 0
								co\KeyPad_CurrButton = 2
							EndIf
						ElseIf GetDPadButtonPress()=180
							co\KeyPad_CurrButton = co\KeyPad_CurrButton + 1
							PlaySound_Strict co\SelectSFX
							co\WaitTimer = fps\Factor[1]
							If co\KeyPad_CurrButton > 2
								co\KeyPad_CurrButton = 0
							EndIf
						EndIf
						
						If GetLeftAnalogStickPitch(True) > 0.0
							co\KeyPad_CurrButton = co\KeyPad_CurrButton - 1
							PlaySound_Strict co\SelectSFX
							co\WaitTimer = fps\Factor[1]
							If co\KeyPad_CurrButton < 0
								co\KeyPad_CurrButton = 2
							EndIf
						ElseIf GetLeftAnalogStickPitch(True) < 0.0
							co\KeyPad_CurrButton = co\KeyPad_CurrButton + 1
							PlaySound_Strict co\SelectSFX
							co\WaitTimer = fps\Factor[1]
							If co\KeyPad_CurrButton > 2
								co\KeyPad_CurrButton = 0
							EndIf
						EndIf
					Else
						If co\WaitTimer > 0.0 And co\WaitTimer < 15.0
							co\WaitTimer = co\WaitTimer + fps\Factor[1]
						ElseIf co\WaitTimer >= 15.0
							co\WaitTimer = 0.0
						EndIf
					EndIf
				EndIf
				
				x=x+120*scale
				y=y+259*scale
				If (Not co\Enabled)
					If RectsOverlap(x,y,82*scale,82*scale,MouseX(),MouseY(),0,0)
						If MouseHit1
							PlaySound_Strict ButtonSFX[0]
							StartNewElevator(d_I\SelectedDoor,3)
							d_I\SelectedDoor = Null
							MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
						EndIf
					EndIf
				Else
					If co\KeyPad_CurrButton = 0
						If JoyHit(CKM_Press)
							PlaySound_Strict ButtonSFX[0]
							StartNewElevator(d_I\SelectedDoor,3)
							d_I\SelectedDoor = Null
							MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
						EndIf
					EndIf
				EndIf
				
				y=y+131*scale
				If (Not co\Enabled)
					If RectsOverlap(x,y,82*scale,82*scale,MouseX(),MouseY(),0,0)
						If MouseHit1
							PlaySound_Strict ButtonSFX[0]
							StartNewElevator(d_I\SelectedDoor,2)
							d_I\SelectedDoor = Null
							MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
						EndIf
					EndIf
				Else
					If co\KeyPad_CurrButton = 1
						If JoyHit(CKM_Press)
							PlaySound_Strict ButtonSFX[0]
							StartNewElevator(d_I\SelectedDoor,2)
							d_I\SelectedDoor = Null
							MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
						EndIf
					EndIf
				EndIf
				
				y=y+130*scale
				If (Not co\Enabled)
					If RectsOverlap(x,y,82*scale,82*scale,MouseX(),MouseY(),0,0)
						If MouseHit1
							PlaySound_Strict ButtonSFX[0]
							StartNewElevator(d_I\SelectedDoor,1)
							d_I\SelectedDoor = Null
							MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
						EndIf
					EndIf
				Else
					If co\KeyPad_CurrButton = 2
						If JoyHit(CKM_Press)
							PlaySound_Strict ButtonSFX[0]
							StartNewElevator(d_I\SelectedDoor,1)
							d_I\SelectedDoor = Null
							MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
						EndIf
					EndIf
				EndIf
			EndIf
			
			If (Not co\Enabled)
				If MouseHit2 Then
					d_I\SelectedDoor = Null
					MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
				EndIf
			Else
				If JoyHit(CKM_Back)
					PlaySound_Strict ButtonSFX[0]
					d_I\SelectedDoor = Null
					MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
				EndIf
			EndIf
		Else
			d_I\SelectedDoor = Null
		EndIf
	Else
		KeypadInput = ""
		KeypadTimer = 0
		KeypadMSG = ""
	EndIf
	
	If (InteractHit(1,CK_Pause) Lor (Steam_GetOverlayUpdated() = 1 And (Not (MenuOpen Lor AttachmentOpen)))) And EndingTimer = 0 Then
		If MenuOpen And (Not InvOpen) Then
			ResumeSounds()
			If OptionsMenu <> 0 Then SaveOptionsINI()
			DeleteMenuGadgets()
			ResetInput()
		Else
			PauseSounds()
		EndIf
		MenuOpen = (Not MenuOpen)
		
		AchievementsMenu = 0
		OptionsMenu = 0
		QuitMSG = 0
		
		d_I\SelectedDoor = Null
		SelectedScreen = Null
		SelectedMonitor = Null
	EndIf
	
	Local spacing%
	Local PrevOtherOpen.Items
	
	Local OtherSize%,OtherAmount%
	
	Local isEmpty%
	
	Local isMouseOn%
	
	Local closedInv%
	
	If OtherOpen<>Null Then
		;[Block]
		If (PlayerRoom\RoomTemplate\Name = "gate_a_topside") Then
			HideEntity Overlay[0]
			CameraFogRange Camera, 5,30
			CameraFogColor (Camera,200,200,200)
			CameraClsColor (Camera,200,200,200)					
			CameraRange(Camera, 0.01, 30)
		ElseIf (PlayerRoom\RoomTemplate\Name = "gate_b_topside") Then
			HideEntity Overlay[0]
			CameraFogRange Camera, 5,45
			CameraFogColor (Camera,200,200,200)
			CameraClsColor (Camera,200,200,200)					
			CameraRange(Camera, 0.01, 60)
		ElseIf (PlayerRoom\RoomTemplate\Name = "gate_a_intro") Then
			CameraFogRange Camera, 5,30
			CameraFogColor (Camera,200,200,200)
			CameraClsColor (Camera,200,200,200)					
			CameraRange(Camera, 0.005, 100)
		EndIf
		
		PrevOtherOpen = OtherOpen
		OtherSize=OtherOpen\invSlots
		
		For i%=0 To OtherSize-1
			If OtherOpen\SecondInv[i] <> Null Then
				OtherAmount = OtherAmount+1
			EndIf
		Next
		
		InvOpen = False
		d_I\SelectedDoor = Null
		Local tempX% = 0
		
		Local width = 70
		Local height = 70
		spacing% = 35
		
		x = opt\GraphicWidth / 2 - (width * MaxItemAmount / 2 + spacing * (MaxItemAmount / 2 - 1)) / 2
		y = opt\GraphicHeight / 2 - (height * OtherSize / 5 + height * (OtherSize / 5 - 1)) / 2
		
		ItemAmount = 0
		For  n% = 0 To OtherSize - 1
			isMouseOn% = False
			If ScaledMouseX() > x And ScaledMouseX() < x + width Then
				If ScaledMouseY() > y And ScaledMouseY() < y + height Then
					isMouseOn = True
				EndIf
			EndIf
			
			If isMouseOn Then
				MouseSlot = n
			EndIf
			
			If OtherOpen = Null Then Exit
			
			DebugLog "otheropen: "+(OtherOpen<>Null)
			If OtherOpen\SecondInv[n] <> Null And SelectedItem <> OtherOpen\SecondInv[n] Then
				If isMouseOn Then
					If SelectedItem = Null Then
						If MouseHit1 Then
							SelectedItem = OtherOpen\SecondInv[n]
							MouseHit1 = False
							
							If DoubleClick Then
								If OtherOpen\SecondInv[n]\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[OtherOpen\SecondInv[n]\itemtemplate\sound])
								OtherOpen = Null
								closedInv=True
								InvOpen = False
								DoubleClick = False
							EndIf
							
						EndIf
					Else
						
					EndIf
				EndIf
				
				ItemAmount=ItemAmount+1
			Else
				If isMouseOn And MouseHit1 Then
					For z% = 0 To OtherSize - 1
						If OtherOpen\SecondInv[z] = SelectedItem Then OtherOpen\SecondInv[z] = Null
					Next
					OtherOpen\SecondInv[n] = SelectedItem
				EndIf
				
			EndIf					
			
			x=x+width + spacing
			tempX=tempX + 1
			If tempX = 5 Then 
				tempX=0
				y = y + height*2 
				x = opt\GraphicWidth / 2 - (width * MaxItemAmount /2 + spacing * (MaxItemAmount / 2 - 1)) / 2
			EndIf
		Next
		
		If SelectedItem <> Null Then
			If MouseDown1 Then
				
			Else
				If MouseSlot = 66 Then
					If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
					
					ShowEntity(SelectedItem\collider)
					PositionEntity(SelectedItem\collider, EntityX(Camera), EntityY(Camera), EntityZ(Camera))
					RotateEntity(SelectedItem\collider, EntityPitch(Camera), EntityYaw(Camera), 0)
					MoveEntity(SelectedItem\collider, 0, -0.1, 0.1)
					RotateEntity(SelectedItem\collider, 0, Rand(360), 0)
					ResetEntity (SelectedItem\collider)
					
					SelectedItem\DropSpeed = 0.0
					
					SelectedItem\Picked = False
					For z% = 0 To OtherSize - 1
						If OtherOpen\SecondInv[z] = SelectedItem Then OtherOpen\SecondInv[z] = Null
					Next
					
					isEmpty=True
					
					For z% = 0 To OtherSize - 1
						If OtherOpen\SecondInv[z]<>Null Then isEmpty=False : Exit
					Next
					
					If isEmpty Then
						Select OtherOpen\itemtemplate\tempname
							Case "clipboard"
								OtherOpen\invimg = OtherOpen\itemtemplate\invimg2
								SetAnimTime OtherOpen\model,17.0
						End Select
					EndIf
					
					SelectedItem = Null
					OtherOpen = Null
					closedInv=True
					
					MoveMouse Viewport_Center_X, Viewport_Center_Y
				Else
					
					If PrevOtherOpen\SecondInv[MouseSlot] = Null Then
						For z% = 0 To OtherSize - 1
							If PrevOtherOpen\SecondInv[z] = SelectedItem Then PrevOtherOpen\SecondInv[z] = Null
						Next
						PrevOtherOpen\SecondInv[MouseSlot] = SelectedItem
						SelectedItem = Null
					ElseIf PrevOtherOpen\SecondInv[MouseSlot] <> SelectedItem
						Select SelectedItem\itemtemplate\tempname
							Default
								CreateMsg(GetLocalString("Items", "cannot_combine"))
						End Select					
					EndIf
					
				EndIf
				SelectedItem = Null
			EndIf
		EndIf
		
		If (closedInv) And (Not InvOpen) Then
			ResumeSounds()
			OtherOpen=Null
			MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
		EndIf
		;[End Block]
		
	ElseIf InvOpen Then
		
		Local keyhits[MaxGunSlots]
		
		If (Not AttachmentOpen) Then
			For i = 0 To MaxGunSlots-1
				keyhits[i] = KeyHit(i + 2)
			Next
		EndIf
		
		d_I\SelectedDoor = Null
		
		width% = 70
		height% = 70
		spacing% = 35
		
		x = opt\GraphicWidth / 2 - (width * MaxItemAmount /2 + spacing * (MaxItemAmount / 2 - 1)) / 2
		y = opt\GraphicHeight / 2 - (height * MaxItemAmount /5 + height * (MaxItemAmount / 5 - 1)) / 2
		
		ItemAmount = 0
		For  n% = 0 To MaxItemAmount - 1
			isMouseOn% = False
			If ScaledMouseX() > x And ScaledMouseX() < x + width Then
				If ScaledMouseY() > y And ScaledMouseY() < y + height Then
					isMouseOn = True
				End If
			EndIf
			
			If isMouseOn Then
				MouseSlot = n
			EndIf
			
			If Inventory[n] <> Null And SelectedItem <> Inventory[n] Then
				If isMouseOn Then
					If SelectedItem = Null Then
						If MouseHit1 Then
							SelectedItem = Inventory[n]
							MouseHit1 = False
							
							If DoubleClick Then
								If Inventory[n]\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[Inventory[n]\itemtemplate\sound])
								InvOpen = False
								DoubleClick = False
							EndIf
						EndIf
						
						x2# = x
						y2# = y+height
						Local width2# = width/3
						Local height2# = height/3
						
						If (Not AttachmentOpen) Then
							
							If Inventory[n]\itemtemplate\isGun% Then
								For i = 0 To MaxGunSlots-1
									If keyhits[i] Then
										If g_I\Weapon_CurrSlot <> (i + 1) Then
											g_I\Weapon_InSlot[i] = Inventory[n]\itemtemplate\tempname
											PlaySound_Strict g_I\UI_Select_SFX
										Else
											PlaySound_Strict g_I\UI_Deny_SFX
										EndIf
									EndIf
								Next
							EndIf
							
						EndIf
					EndIf
				EndIf
				
				ItemAmount=ItemAmount+1
			Else
				If isMouseOn And MouseHit1 Then
					For z% = 0 To MaxItemAmount - 1
						If Inventory[z] = SelectedItem Then Inventory[z] = Null
					Next
					Inventory[n] = SelectedItem
				End If
				
			EndIf					
			
			x=x+width + spacing
			If n = 4 Then 
				y = y + height*2 
				x = opt\GraphicWidth / 2 - (width * MaxItemAmount /2 + spacing * (MaxItemAmount / 2 - 1)) / 2
			EndIf
			
			width2# = 70
			height2# = 70
			Local spacing2# = 35
			
			x2# = opt\GraphicWidth / 2
			y2# = opt\GraphicHeight / 2 + height2*2.6
			
			x2 = opt\GraphicWidth / 2 - (width2 * MaxItemAmount /2 + spacing2 * (MaxItemAmount / 2 - 1)) / 2
			y2 = opt\GraphicHeight / 2 - height2 + height2*4
			
			x2=x2+width2 + spacing2
			
			x2=x2+width2 + spacing2
			
			x2=x2+width2 + spacing2
			
		Next
		
		If SelectedItem <> Null Then
			If (Not MouseDown1) Lor MouseHit2 Then
				If MouseSlot = 66 Then
					Select SelectedItem\itemtemplate\tempname
						Case "hazmatsuit", "hazmatsuit2", "hazmatsuit3"
							;[Block]
							If wbi\Hazmat > 0 Then
								CreateHintMsg(GetLocalString("Items", "doubleclick_to_take_off"))
								Return
							EndIf
							;[End Block]
						Case "helmet", "finehelmet"
							;[Block]
							If wbi\Helmet > 0 Then
								CreateHintMsg(GetLocalString("Items", "doubleclick_to_take_off"))
								Return
							EndIf
							;[End Block]
						Case "ntf_helmet", "fine_ntf_helmet"
							;[Block]
							If mpl\HasNTFGasmask > 0 Then
								CreateHintMsg(GetLocalString("Items", "doubleclick_to_take_off"))
								Return
							EndIf
							;[End Block]
						Case "gasmask", "supergasmask","gasmask3"
							;[Block]
							If wbi\GasMask > 0 Then
								CreateHintMsg(GetLocalString("Items", "doubleclick_to_take_off"))
								Return
							EndIf
							;[End Block]
						Case "scramble","finescramble"
							;[Block]
							If wbi\SCRAMBLE > 0 Then
								CreateHintMsg(GetLocalString("Items", "doubleclick_to_take_off"))
								Return
							EndIf
							;[End Block]
						Case "nvgoggles","supernv","finenvgoggles"
							;[Block]
							If wbi\NightVision > 0 Then
								CreateHintMsg(GetLocalString("Items", "doubleclick_to_take_off"))
								Return
							EndIf
							;[End Block]
						Case "scp198"
						    ;[Block]
							CreateHintMsg(GetLocalString("Items", "scp198_1"))
							Return
						    ;[End Block]
						Case "scp357"
						    ;[Block]
						    If I_357\Timer >= 56.0 And I_357\Using Then
								CreateHintMsg(GetLocalString("Items", "scp357_1"))
								Return
							EndIf 
				            ;[End Block]
					End Select
					
					Local isOverQuickSelection% = 0
					width2# = 70
					height2# = 70
					spacing2# = 35
					x2 = opt\GraphicWidth / 2 - (width2 * MaxItemAmount /2 + spacing2 * (MaxItemAmount / 2 - 1)) / 2
					y2 = opt\GraphicHeight / 2 - height2 + height2*4
					x2=x2+width2 + spacing2
					For i = 0 To MaxGunSlots-1
						If MouseOn(x2+((width2+spacing2)*i), y2, width2, height2) Then
							isOverQuickSelection = (i + 1)
							Exit
						EndIf
					Next
					
					If (Not isOverQuickSelection) Then
						DropItem(SelectedItem)
						
						InvOpen = False
						
						MoveMouse Viewport_Center_X, Viewport_Center_Y
					Else
						If SelectedItem\itemtemplate\isGun Then
							If isOverQuickSelection = g_I\Weapon_CurrSlot Then
								PlaySound_Strict g_I\UI_Deny_SFX
							Else
								g_I\Weapon_InSlot[isOverQuickSelection - 1] = SelectedItem\itemtemplate\tempname
								PlaySound_Strict g_I\UI_Select_SFX
							EndIf
						Else
							CreateHintMsg(GetLocalString("Items", "cannot_nonweapon"))
						EndIf
					EndIf
					SelectedItem = Null	
				Else
					If Inventory[MouseSlot] = Null Then
						For z% = 0 To MaxItemAmount - 1
							If Inventory[z] = SelectedItem Then Inventory[z] = Null
						Next
						Inventory[MouseSlot] = SelectedItem
						SelectedItem = Null
					ElseIf Inventory[MouseSlot] <> SelectedItem
						Select SelectedItem\itemtemplate\tempname
							Case "scopebat"
								;[Block]
								For g.Guns = Each Guns
									If g\name$ = Inventory[MouseSlot]\itemtemplate\tempname$
										If g\HasAcog And g\ScopeCharge# >= ScopeChargeTime# Then
											RemoveItem(SelectedItem)
											g\ScopeCharge# = 0.0
											CreateMsg(GetLocalStringR("Items", "scope_battery_1",Inventory[MouseSlot]\itemtemplate\name)+".")
										ElseIf g\ScopeCharge# <= ScopeChargeTime# Then
											CreateMsg(GetLocalString("Items", "scope_battery_2"))
										Else
											CreateMsg(GetLocalString("Items", "scope_battery_3"))
										EndIf
									EndIf
								Next
								;[End Block]
							Case "ammocrate","bigammocrate"
								;[Block]
								If Inventory[MouseSlot]\itemtemplate\isGun = True Then
									If Inventory[MouseSlot]\itemtemplate\tempname<>"crowbar" And Inventory[MouseSlot]\itemtemplate\tempname<>"knife" And Inventory[MouseSlot]\itemtemplate\tempname<>"grenade" And Inventory[MouseSlot]\itemtemplate\tempname<>"scp127" Then
										For g = Each Guns
											If g_I\HoldingGun = g\ID Then
												If g\CurrReloadAmmo < g\MaxReloadAmmo Then
													If SelectedItem\itemtemplate\tempname = "ammocrate" Then
														g\CurrReloadAmmo = Min(g\CurrReloadAmmo+g\MaxReloadAmmo*2,g\MaxReloadAmmo)
													Else
														g\CurrReloadAmmo = Min(g\CurrReloadAmmo+g\MaxReloadAmmo*4,g\MaxReloadAmmo)
													EndIf
													CreateMsg(GetLocalString("Weapons","ammo_picked"))
													RemoveItem(SelectedItem)
													SelectedItem = Null
													Inventory[MouseSlot]\state = 100.0
												Else
													CreateMsg(GetLocalString("Weapons","enough_ammo"))
												EndIf
											EndIf
										Next
									EndIf
								EndIf
								;[End Block]
							Case "grenade_mag"
								;[Block]
								If Inventory[MouseSlot]\itemtemplate\isGun = True Then
									If Inventory[MouseSlot]\itemtemplate\tempname = "xm29" Then
										For g = Each Guns
											If g_I\HoldingGun = g\ID Then
												If g\CurrReloadAltAmmo < g\MaxReloadAltAmmo Then
													g\CurrReloadAltAmmo = Min(g\CurrReloadAltAmmo+g\MaxReloadAltAmmo,g\MaxReloadAltAmmo)
													CreateMsg(GetLocalString("Items","grenade_mag_picked"))
													RemoveItem(SelectedItem)
													SelectedItem = Null
													Inventory[MouseSlot]\state = 100.0
												Else
													CreateMsg(GetLocalString("Items","cant_pick_grenade_mag"))
												EndIf
											EndIf
										Next
									EndIf
								EndIf
								;[End Block]
							Case "emr-p_mag"
								;[Block]
								If Inventory[MouseSlot]\itemtemplate\isGun = True Then
									If Inventory[MouseSlot]\itemtemplate\tempname = "emr-p" Then
										For g = Each Guns
											If g_I\HoldingGun = g\ID Then
												If g\CurrReloadAmmo < g\MaxReloadAmmo Then
													g\CurrReloadAmmo = Min(g\CurrReloadAmmo+10,g\MaxReloadAmmo)
													CreateMsg(GetLocalString("Items","emrp_mag_picked"))
													RemoveItem(SelectedItem)
													SelectedItem = Null
													Inventory[MouseSlot]\state = 100.0
												Else
													CreateMsg(GetLocalString("Items","cant_pick_emrp_mag"))
												EndIf
											EndIf
										Next
									EndIf
								EndIf
								;[End Block]
							Case "battery", "bat"
								;[Block]
								Select Inventory[MouseSlot]\itemtemplate\name
									Case "S-NAV Navigator", "S-NAV 300 Navigator", "S-NAV 310 Navigator"
										If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])	
										RemoveItem (SelectedItem)
										SelectedItem = Null
										Inventory[MouseSlot]\state = 100.0
										CreateMsg(GetLocalString("Items", "battery_nav"))
									Case "S-NAV Navigator Ultimate"
										CreateMsg(GetLocalString("Items", "battery_nav_noplace"))
									Case "Radio Transceiver"
										Select Inventory[MouseSlot]\itemtemplate\tempname 
											Case "fineradio", "veryfineradio"
												CreateMsg(GetLocalString("Items", "battery_radio_noplace"))
											Case "18vradio"
												CreateMsg(GetLocalString("Items", "battery_radio_18v"))
											Case "radio"
												If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])	
												RemoveItem (SelectedItem)
												SelectedItem = Null
												Inventory[MouseSlot]\state = 100.0
												CreateMsg(GetLocalString("Items", "battery_radio"))
										End Select
									Case "Night Vision Goggles"
										Local nvname$ = Inventory[MouseSlot]\itemtemplate\tempname
										If nvname$="nvgoggles" Lor nvname$="supernv" Then
											If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])	
											RemoveItem (SelectedItem)
											SelectedItem = Null
											Inventory[MouseSlot]\state = 1000.0
											CreateMsg(GetLocalString("Items", "battery_nvg"))
										Else
											CreateMsg(GetLocalString("Items", "battery_nvg_noplace"))
										EndIf
									Case "SCRAMBLE Gear"
										nvname$ = Inventory[MouseSlot]\itemtemplate\tempname
										If nvname$="scramble" Then
											If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])	
											RemoveItem (SelectedItem)
											SelectedItem = Null
											Inventory[MouseSlot]\state = 1000.0
											CreateMsg(GetLocalString("Items", "battery_scramble"))
										Else
											CreateMsg(GetLocalString("Items", "battery_scramble_noplace"))
										EndIf
									Default
										CreateHintMsg(GetLocalString("Items", "cannot_combine"))
								End Select
								;[End Block]
							Case "18vbat"
								;[Block]
								Select Inventory[MouseSlot]\itemtemplate\name
									Case "S-NAV Navigator", "S-NAV 300 Navigator", "S-NAV 310 Navigator"
										CreateMsg(GetLocalString("Items", "battery_nav_18v"))
									Case "S-NAV Navigator Ultimate"
										CreateMsg(GetLocalString("Items", "battery_nav_noplace"))
									Case "Radio Transceiver"
										Select Inventory[MouseSlot]\itemtemplate\tempname 
											Case "fineradio", "veryfineradio"
												CreateMsg(GetLocalString("Items", "battery_radio_noplace"))
											Case "18vradio"
												If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])	
												RemoveItem (SelectedItem)
												SelectedItem = Null
												Inventory[MouseSlot]\state = 100.0
												CreateMsg(GetLocalString("Items", "battery_radio"))
										End Select 
									Default
										CreateHintMsg(GetLocalString("Items", "cannot_combine"))
								End Select
								;[End Block]
							Case "paper","oldpaper","badge","key1","key2","key3","key4","key5","key6","key_cave","key_cave2","misc","ticket","25ct","coin","vanecoin"
								;[Block]
								Local c,ri
								
								If Inventory[MouseSlot]\itemtemplate\tempname = "clipboard" Then
									Local added.Items = Null
									If SelectedItem\itemtemplate\tempname<>"misc" Lor (SelectedItem\itemtemplate\name="Playing Card" Lor SelectedItem\itemtemplate\name="Mastercard") Then
										For c% = 0 To Inventory[MouseSlot]\invSlots-1
											If (Inventory[MouseSlot]\SecondInv[c] = Null)
												If SelectedItem <> Null Then
													Inventory[MouseSlot]\SecondInv[c] = SelectedItem
													Inventory[MouseSlot]\state = 1.0
													SetAnimTime Inventory[MouseSlot]\model,0.0
													Inventory[MouseSlot]\invimg = Inventory[MouseSlot]\itemtemplate\invimg
													
													For ri% = 0 To MaxItemAmount - 1
														If Inventory[ri] = SelectedItem Then
															Inventory[ri] = Null
															PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
														EndIf
													Next
													added = SelectedItem
													SelectedItem = Null : Exit
												EndIf
											EndIf
										Next
										If SelectedItem <> Null Then
											CreateMsg(GetLocalString("Items", "clipboard_notstrong"))
										Else
											If added\itemtemplate\tempname = "paper" Lor added\itemtemplate\tempname = "oldpaper" Then
												CreateMsg(GetLocalString("Items", "clipboard_added_1"))
											ElseIf added\itemtemplate\tempname = "badge"
												CreateMsg(GetLocalStringR("Items", "clipboard_added_2",added\itemtemplate\name))
											Else
												CreateMsg(GetLocalStringR("Items", "clipboard_added_3",added\itemtemplate\name))
											EndIf
										EndIf
									Else
										CreateHintMsg(GetLocalString("Items", "cannot_combine"))
									EndIf
								ElseIf Inventory[MouseSlot]\itemtemplate\tempname = "wallet" Then
									added.Items = Null
									If SelectedItem\itemtemplate\tempname<>"misc" Lor (SelectedItem\itemtemplate\name="Playing Card" Lor SelectedItem\itemtemplate\name="Mastercard") And SelectedItem\itemtemplate\tempname<>"paper" And SelectedItem\itemtemplate\tempname<>"oldpaper" And SelectedItem\itemtemplate\tempname<>"ticket" Then
										For c% = 0 To Inventory[MouseSlot]\invSlots-1
											If (Inventory[MouseSlot]\SecondInv[c] = Null)
												If SelectedItem <> Null Then
													Inventory[MouseSlot]\SecondInv[c] = SelectedItem
													Inventory[MouseSlot]\state = 1.0
													SetAnimTime Inventory[MouseSlot]\model,0.0
													Inventory[MouseSlot]\invimg = Inventory[MouseSlot]\itemtemplate\invimg
													
													For ri% = 0 To MaxItemAmount - 1
														If Inventory[ri] = SelectedItem Then
															Inventory[ri] = Null
															PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
														EndIf
													Next
													added = SelectedItem
													SelectedItem = Null : Exit
												EndIf
											EndIf
										Next
										If SelectedItem <> Null Then
											CreateMsg(GetLocalString("Items", "wallet_notbig"))
										Else
											CreateMsg(GetLocalStringR("Items", "wallet_added",added\itemtemplate\name))
										EndIf
									Else
										CreateHintMsg(GetLocalString("Items", "cannot_combine"))
									EndIf
								EndIf
								SelectedItem = Null
								;[End Block]
							Case "pill","scp500pill","scp500death","scp005","scp714","scp402","420","420s","cigarette","scp860","scp1079sweet","fuse","fuse_purple"
								;[Block]
								If Inventory[MouseSlot]\itemtemplate\tempname = "wallet" Then
									added.Items = Null
									If SelectedItem\itemtemplate\tempname<>"misc" Lor (SelectedItem\itemtemplate\name="Playing Card" Lor SelectedItem\itemtemplate\name="Mastercard") Then
										For c% = 0 To Inventory[MouseSlot]\invSlots-1
											If (Inventory[MouseSlot]\SecondInv[c] = Null)
												If SelectedItem <> Null Then
													Inventory[MouseSlot]\SecondInv[c] = SelectedItem
													Inventory[MouseSlot]\state = 1.0
													SetAnimTime Inventory[MouseSlot]\model,0.0
													Inventory[MouseSlot]\invimg = Inventory[MouseSlot]\itemtemplate\invimg
													
													For ri% = 0 To MaxItemAmount - 1
														If Inventory[ri] = SelectedItem Then
															Inventory[ri] = Null
															PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
														EndIf
													Next
													added = SelectedItem
													SelectedItem = Null : Exit
												EndIf
											EndIf
										Next
										If SelectedItem <> Null Then
											CreateMsg(GetLocalString("Items", "wallet_notbig"))
										Else
											CreateMsg(GetLocalStringR("Items", "wallet_added",added\itemtemplate\name))
										EndIf
									Else
										CreateHintMsg(GetLocalString("Items", "cannot_combine"))
									EndIf
								EndIf
								SelectedItem = Null
								;[End Block]
							Default
								;[Block]
								CreateHintMsg(GetLocalString("Items", "cannot_combine"))
								;[End Block]
						End Select
						
					EndIf
					
				EndIf
				SelectedItem = Null
			EndIf
		EndIf
		
		If InvOpen = False Then 
			ResumeSounds()
			ResetInput()
		EndIf
	Else
		If SelectedItem <> Null Then
			Select SelectedItem\itemtemplate\tempname
;! ~ Wearible - Items
				Case "scramble","veryfinescramble"
					;[Block]
					If (Not hds\Wearing) Then
						If (Not wbi\Hazmat Lor wbi\Helmet Lor wbi\NightVision Lor wbi\GasMask Lor mpl\HasNTFGasmask) Then
							CurrSpeed = CurveValue(0.0, CurrSpeed, 5.0)
							
							SelectedItem\state2 = Min(SelectedItem\state2 + (fps\Factor[0]), 100.0)
							
							If SelectedItem\state2 = 100.0 Then
								If wbi\SCRAMBLE > 0 Then
									CreateMsg(GetLocalString("Items", "scramble_off"))
									wbi\SCRAMBLE = 0
								Else
									If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
									CreateMsg(GetLocalString("Items", "scramble_on"))
									Select SelectedItem\itemtemplate\tempname
										Case "scramble"
											;[Block]
											wbi\SCRAMBLE = 1
											;[End Block]
										Case "veryfinescramble"
											;[Block]
											wbi\SCRAMBLE = 2
											;[End Block]
									End Select
								EndIf
								SelectedItem\state2 = 0.0
								SelectedItem = Null
							EndIf
						Else
							CreateMsg(GetLocalString("Items", "scramble_cant"))
						EndIf
					Else
						CreateMsg(GetLocalString("Items", "scramble_cant_with_hds"))
					EndIf
					;[End Block]
				Case "nvgoggles","supernv","finenvgoggles"
					;[Block]
					If (Not hds\Wearing) Then
						If (Not wbi\Hazmat Lor wbi\Helmet Lor wbi\SCRAMBLE Lor wbi\GasMask Lor mpl\HasNTFGasmask) Then
							CurrSpeed = CurveValue(0.0, CurrSpeed, 5.0)
							
							SelectedItem\state2 = Min(SelectedItem\state2 + (fps\Factor[0]), 100.0)
							
							If SelectedItem\state2 = 100.0 Then
								If wbi\NightVision > 0 Then
									CreateMsg(GetLocalString("Items", "nvg_off"))
									CameraFogFar = StoredCameraFogFar
									wbi\NightVision = 0
								Else
									If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
									CreateMsg(GetLocalString("Items", "nvg_on"))
									StoredCameraFogFar = CameraFogFar
									CameraFogFar = 30
									Select SelectedItem\itemtemplate\tempname
										Case "nvgoggles"
											;[Block]
											wbi\NightVision = 1
											;[End Block]
										Case "supernv"
											;[Block]
											wbi\NightVision = 2
											;[End Block]
										Case "finenvgoggles"
											;[Block]
											wbi\NightVision = 3
											;[End Block]
									End Select
								EndIf
								SelectedItem\state2 = 0.0
								SelectedItem = Null
							EndIf
						Else
							CreateMsg(GetLocalString("Items", "nvg_cant"))
						EndIf
					Else
						CreateMsg(GetLocalString("Items", "nvg_cant_with_hds"))
					EndIf
					;[End Block]
				Case "hazmatsuit", "hazmatsuit2", "hazmatsuit3"
					;[Block]
					If (Not hds\Wearing) Then
						If (Not wbi\Helmet Lor wbi\GasMask Lor wbi\NightVision Lor wbi\SCRAMBLE Lor I_268\Using Lor mpl\HasNTFGasmask) Then
							CurrSpeed = CurveValue(0.0, CurrSpeed, 5.0)
							
							SelectedItem\state = Min(SelectedItem\state + (fps\Factor[0] / 4.0), 100.0)
							
							If wbi\Hazmat > 0 Then
								If gc\CurrZone = BCZ And (Not ecst\WasInBCZ) Then
									CreateMsg(GetLocalString("Items", "wont_hazmat_off"))
									SelectedItem\state = 0.0
								EndIf
							EndIf
							
							If SelectedItem\state = 100.0 Then
								If wbi\Hazmat > 0 Then
									If gc\CurrZone = BCZ And (Not ecst\WasInBCZ) Then
										CreateMsg(GetLocalString("Items", "wont_hazmat_off"))
									Else
										CreateMsg(GetLocalString("Items", "hazmat_off"))
										wbi\Hazmat = 0
										DropItem(SelectedItem)
									EndIf
								Else
									If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
									CreateMsg(GetLocalString("Items", "hazmat_on"))
									Select SelectedItem\itemtemplate\tempname
										Case "hazmatsuit"
											;[Block]
											wbi\Hazmat = 1
											;[End Block]
										Case "hazmatsuit2"
											;[Block]
											wbi\Hazmat = 2
											;[End Block]
										Case "hazmatsuit3"
											;[Block]
											wbi\Hazmat = 3
											;[End Block]
									End Select
								EndIf
								SelectedItem\state = 0.0
								SelectedItem = Null
							EndIf
						Else
							CreateMsg(GetLocalString("Items", "hazmat_cant"))
						EndIf
					Else
						CreateMsg(GetLocalString("Items", "hazmat_cant_with_hds"))
					EndIf
					;[End Block]
				Case "ntf_helmet","fine_ntf_helmet"
					;[Block]
					If (Not hds\Wearing) Then
						If (Not wbi\Hazmat Lor wbi\GasMask Lor wbi\NightVision Lor wbi\SCRAMBLE Lor I_268\Using Lor wbi\Helmet) Then
							CurrSpeed = CurveValue(0.0, CurrSpeed, 5.0)
							
							SelectedItem\state = Min(SelectedItem\state + (fps\Factor[0]), 100.0)
							
							If SelectedItem\state = 100.0 Then
								If mpl\HasNTFGasmask > 0 Then
									CreateMsg(GetLocalString("Items", "helmet_off"))
									mpl\HasNTFGasmask = 0
								Else
									If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
									CreateMsg(GetLocalString("Items", "helmet_on"))
									Select SelectedItem\itemtemplate\tempname
										Case "ntf_helmet"
											;[Block]
											mpl\HasNTFGasmask = 1
											If SelectedItem\state2 = 0 Then
												psp\Helmet = 150
												SelectedItem\state2 = 1
											EndIf
											;[End Block]
										Case "fine_ntf_helmet"
											;[Block]
											mpl\HasNTFGasmask = 2
											If SelectedItem\state2 = 0 Then
												psp\Helmet = 200
												SelectedItem\state2 = 1
											EndIf
											;[End Block]
									End Select
								EndIf
								psp\Helmet = psp\Helmet
								SelectedItem\state = 0.0
								SelectedItem = Null
							EndIf
						Else
							CreateMsg(GetLocalString("Items", "helmet_cant"))
						EndIf
					Else
						CreateMsg(GetLocalString("Items", "helmet_cant_with_hds"))
					EndIf
					;[End Block]
				Case "helmet","finehelmet"
					;[Block]
					If (Not hds\Wearing) Then
						If (Not wbi\Hazmat Lor wbi\GasMask Lor wbi\NightVision Lor wbi\SCRAMBLE Lor I_268\Using Lor mpl\HasNTFGasmask) Then
							CurrSpeed = CurveValue(0.0, CurrSpeed, 5.0)
							
							SelectedItem\state = Min(SelectedItem\state + (fps\Factor[0]), 100.0)
							
							If SelectedItem\state = 100.0 Then
								If wbi\Helmet > 0 Then
									CreateMsg(GetLocalString("Items", "helmet_off"))
									wbi\Helmet = 0
								Else
									If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
									CreateMsg(GetLocalString("Items", "helmet_on"))
									Select SelectedItem\itemtemplate\tempname
										Case "helmet"
											;[Block]
											wbi\Helmet = 1
											If SelectedItem\state2 = 0 Then
												psp\Helmet = 100
												SelectedItem\state2 = 1
											EndIf
											;[End Block]
										Case "finehelmet"
											;[Block]
											wbi\Helmet = 2
											If SelectedItem\state2 = 0 Then
												psp\Helmet = 150
												SelectedItem\state2 = 1
											EndIf
											;[End Block]
									End Select
								EndIf
								psp\Helmet = psp\Helmet
								SelectedItem\state = 0.0
								SelectedItem = Null
							EndIf
						Else
							CreateMsg(GetLocalString("Items", "helmet_cant"))
						EndIf
					Else
						CreateMsg(GetLocalString("Items", "helmet_cant_with_hds"))
					EndIf
					;[End Block]
				Case "gasmask", "supergasmask", "gasmask3"
					;[Block]
					If (Not hds\Wearing) Then
						If (Not wbi\Hazmat Lor wbi\Helmet Lor wbi\NightVision Lor wbi\SCRAMBLE Lor I_268\Using Lor mpl\HasNTFGasmask) Then
							CurrSpeed = CurveValue(0.0, CurrSpeed, 5.0)
							
							SelectedItem\state = Min(SelectedItem\state + (fps\Factor[0]), 100.0)
							
							If SelectedItem\state = 100.0 Then
								If wbi\GasMask > 0 Then
									CreateMsg(GetLocalString("Items", "mask_off"))
									wbi\GasMask = 0
								Else
									If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
									CreateMsg(GetLocalString("Items", "mask_on"))
									Select SelectedItem\itemtemplate\tempname
										Case "gasmask"
											;[Block]
											wbi\GasMask = 1
											;[End Block]
										Case "supergasmask"
											;[Block]
											wbi\GasMask = 2
											;[End Block]
										Case "gasmask3"
											;[Block]
											wbi\GasMask = 3
											;[End Block]
									End Select
								EndIf
								SelectedItem\state = 0.0
								SelectedItem = Null
							EndIf
						Else
							CreateMsg(GetLocalString("Items", "mask_cant"))
						EndIf
					Else
						CreateMsg(GetLocalString("Items", "mask_cant_with_hds"))
					EndIf
					;[End Block]
;! ~ Nostalgia - Items
				Case "vanecoin"
					;[Block]
					If SelectedItem\state = 0 Then
						PlaySound_Strict LoadTempSound("SFX\SCP\1162\NostalgiaCancer"+Rand(6,10)+".ogg")
						CreateMsg(Chr(34)+GetLocalString("Items","vane")+Chr(34))
						SelectedItem\state = 1
					EndIf
					;[End Block]
;! ~ Devices - Items
				Case "radio","18vradio","fineradio","veryfineradio"
					;[Block]
					If SelectedItem\state <= 100 Then SelectedItem\state = Max(0, SelectedItem\state - fps\Factor[0] * 0.004)
					
					If RadioState[5] = 0 Then
						If SelectedItem\state > 0 Then
							RadioState[5] = 1
						EndIf
						RadioState[0] = -1
					EndIf
					
					x = opt\GraphicWidth - ImageWidth(SelectedItem\itemtemplate\img)
					y = opt\GraphicHeight - ImageHeight(SelectedItem\itemtemplate\img)
					
					If SelectedItem\state > 0 Then
						If PlayerRoom\RoomTemplate\Name = "pocketdimension" Lor CoffinDistance < 4.0 Then
							ResumeChannel(RadioCHN[5])
							If ChannelPlaying(RadioCHN[5]) = False Then RadioCHN[5] = PlaySound_Strict(RadioStatic)	
						Else
							Select Int(SelectedItem\state2)
								Case 0
									ResumeChannel(RadioCHN[0])
									If (Not EnableUserTracks)
										If ChannelPlaying(RadioCHN[0]) = False Then RadioCHN[0] = PlaySound_Strict(RadioStatic)
										StrTemp = StrTemp + GetLocalString("Devices","radio_track_disabled")
									ElseIf UserTrackMusicAmount<1
										If ChannelPlaying(RadioCHN[0]) = False Then RadioCHN[0] = PlaySound_Strict(RadioStatic)
										StrTemp = StrTemp + GetLocalString("Devices","radio_no_track")
									Else
										If (Not ChannelPlaying(RadioCHN[0]))
											If (Not UserTrackFlag%)
												If UserTrackMode
													If RadioState[0]<(UserTrackMusicAmount-1)
														RadioState[0] = RadioState[0] + 1
													Else
														RadioState[0] = 0
													EndIf
													UserTrackFlag = True
												Else
													RadioState[0] = Rand(0,UserTrackMusicAmount-1)
												EndIf
											EndIf
											If CurrUserTrack%<>0 Then FreeSound_Strict(CurrUserTrack%) : CurrUserTrack% = 0
											CurrUserTrack% = LoadSound_Strict("SFX\Radio\UserTracks\"+UserTrackName[RadioState[0]])
											RadioCHN[0] = PlaySound_Strict(CurrUserTrack%)
											DebugLog "CurrTrack: "+RadioState[0]
											DebugLog UserTrackName[RadioState[0]]
										Else
											StrTemp = StrTemp + Upper(UserTrackName[RadioState[0]]) + "          "
											UserTrackFlag = False
										EndIf
										
										If KeyHit(2) Then
											PlaySound_Strict RadioSquelch
											If (Not UserTrackFlag%)
												If UserTrackMode
													If RadioState[0]<(UserTrackMusicAmount-1)
														RadioState[0] = RadioState[0] + 1
													Else
														RadioState[0] = 0
													EndIf
													UserTrackFlag = True
												Else
													RadioState[0] = Rand(0,UserTrackMusicAmount-1)
												EndIf
											EndIf
											If CurrUserTrack%<>0 Then FreeSound_Strict(CurrUserTrack%) : CurrUserTrack% = 0
											CurrUserTrack% = LoadSound_Strict("SFX\Radio\UserTracks\"+UserTrackName[RadioState[0]])
											RadioCHN[0] = PlaySound_Strict(CurrUserTrack%)
											DebugLog "CurrTrack: "+RadioState[0]
											DebugLog UserTrackName[RadioState[0]]
										EndIf
									EndIf
								Case 1
									ResumeChannel(RadioCHN[1])
									If ChannelPlaying(RadioCHN[1]) = False Then
										If RadioState[1] => 5 Then
											RadioCHN[1] = PlaySound_Strict(RadioSFX[0 * 10 + 1])	
											RadioState[1] = 0
										Else
											RadioState[1]=RadioState[1]+1	
											RadioCHN[1] = PlaySound_Strict(RadioSFX[0 * 10])	
										EndIf
									EndIf
								Case 2
									ResumeChannel(RadioCHN[2])
									If ChannelPlaying(RadioCHN[2]) = False Then
										RadioState[2]=RadioState[2]+1
										If RadioState[2] = 17 Then RadioState[2] = 1
										If Floor(RadioState[2]/2)=Ceil(RadioState[2]/2) Then
											RadioCHN[2] = PlaySound_Strict(RadioSFX[1 * 10 + Int(RadioState[2]/2)])
										Else
											RadioCHN[2] = PlaySound_Strict(RadioSFX[1 * 10])
										EndIf
									EndIf 
								Case 3
									ResumeChannel(RadioCHN[3])
									If ChannelPlaying(RadioCHN[3]) = False Then RadioCHN[3] = PlaySound_Strict(RadioStatic)
									If (Not ecst\CIArrived) Then
										If ecst\NTFArrived Then
											If MTFtimer > 0 Then 
												RadioState[3]=RadioState[3]+Max(Rand(-10,1),0)
												Select RadioState[3]
													Case 40
														If Not RadioState3[0] Then
															RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random1.ogg"))
															RadioState[3] = RadioState[3]+1	
															RadioState3[0] = True	
														EndIf											
													Case 400
														If Not RadioState3[1] Then
															RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random2.ogg"))
															RadioState[3] = RadioState[3]+1	
															RadioState3[1] = True	
														EndIf	
													Case 800
														If Not RadioState3[2] Then
															RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random3.ogg"))
															RadioState[3] = RadioState[3]+1	
															RadioState3[2] = True
														EndIf													
													Case 1200
														If Not RadioState3[3] Then
															RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random4.ogg"))	
															RadioState[3] = RadioState[3]+1	
															RadioState3[3] = True
														EndIf
													Case 1600
														If Not RadioState3[4] Then
															RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random5.ogg"))	
															RadioState[3] = RadioState[3]+1
															RadioState3[4] = True
														EndIf
													Case 2000
														If Not RadioState3[5] Then
															RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random6.ogg"))	
															RadioState[3] = RadioState[3]+1
															RadioState3[5] = True
														EndIf
													Case 2400
														If Not RadioState3[6] Then
															RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random7.ogg"))	
															RadioState[3] = RadioState[3]+1
															RadioState3[6] = True
														EndIf
												End Select
											EndIf
										EndIf
									Else
										RadioState[3]=RadioState[3]+Max(Rand(-10,1),0)
										Select RadioState[3]
											Case 40
												If Not RadioState3[0] Then
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\CI\Dialogues\Random_1.ogg"))
													RadioState[3] = RadioState[3]+1	
													RadioState3[0] = True	
												EndIf											
											Case 400
												If Not RadioState3[1] Then
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\CI\Dialogues\Random_2.ogg"))
													RadioState[3] = RadioState[3]+1	
													RadioState3[1] = True	
												EndIf	
											Case 800
												If Not RadioState3[2] Then
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\CI\Dialogues\Random_3.ogg"))
													RadioState[3] = RadioState[3]+1	
													RadioState3[2] = True
												EndIf													
											Case 1200
												If Not RadioState3[3] Then
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Dialogues\Secret.ogg"))	
													RadioState[3] = RadioState[3]+1	
													RadioState3[3] = True
												EndIf
										End Select
									EndIf
								Case 4
									ResumeChannel(RadioCHN[6])
									If ChannelPlaying(RadioCHN[6]) = False Then RadioCHN[6] = PlaySound_Strict(RadioStatic)
									ResumeChannel(RadioCHN[4])
									If ChannelPlaying(RadioCHN[4]) = False Then 
										If RemoteDoorOn = False And RadioState[8] = False Then
											RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\Chatter3.ogg"))	
											RadioState[8] = True
										Else
											RadioState[4]=RadioState[4]+Max(Rand(-10,1),0)
											Select RadioState[4]
												Case 10
													If (Not Contained106)
														If Not RadioState4[0] Then
															RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\OhGod.ogg"))
															RadioState[4] = RadioState[4]+1
															RadioState4[0] = True
														EndIf
													EndIf													
												Case 100
													If Not RadioState4[1] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\Chatter2.ogg"))
														RadioState[4] = RadioState[4]+1
														RadioState4[1] = True
													EndIf		
												Case 158
													If MTFtimer = 0 Then 
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\franklin1.ogg"))
														RadioState[4] = RadioState[4]+1
														RadioState[2] = True
													EndIf
												Case 200
													If Not RadioState4[3] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\Chatter4.ogg"))
														RadioState[4] = RadioState[4]+1
														RadioState4[3] = True
													EndIf		
												Case 260
													If Not RadioState4[4] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\SCP\035\RadioHelp1.ogg"))
														RadioState[4] = RadioState[4]+1
														RadioState4[4] = True
													EndIf		
												Case 300
													If Not RadioState4[5] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\Chatter1.ogg"))	
														RadioState[4] = RadioState[4]+1	
														RadioState4[5] = True
													EndIf		
												Case 350
													If Not RadioState4[6] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\franklin2.ogg"))
														RadioState[4] = RadioState[4]+1
														RadioState4[6] = True
													EndIf		
												Case 400
													If Not RadioState4[7] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\SCP\035\RadioHelp2.ogg"))
														RadioState[4] = RadioState[4]+1
														RadioState4[7] = True
													EndIf		
												Case 450
													If Not RadioState4[8] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\franklin3.ogg"))	
														RadioState[4] = RadioState[4]+1		
														RadioState4[8] = True
													EndIf		
												Case 600
													If Not RadioState4[2] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\franklin4.ogg"))	
														RadioState[4] = RadioState[4]+1	
														RadioState4[2] = True
													EndIf		
											End Select
										EndIf
									EndIf
								Case 5
									ResumeChannel(RadioCHN[5])
									If ChannelPlaying(RadioCHN[5]) = False Then RadioCHN[5] = PlaySound_Strict(RadioStatic)
							End Select 
							
							x=x+66
							y=y+419
							
							Color (30,30,30)
							
							If SelectedItem\state <= 100 Then
								For i = 0 To 4
									Rect(x, y+8*i, 43 - i * 6, 4, Ceil(SelectedItem\state / 20.0) > 4 - i )
								Next
							EndIf	
							
							If SelectedItem\itemtemplate\tempname = "veryfineradio" Then
								ResumeChannel(RadioCHN[0])
								If ChannelPlaying(RadioCHN[0]) = False Then RadioCHN[0] = PlaySound_Strict(RadioStatic)
								RadioState[6]=RadioState[6] + fps\Factor[0]
								temp = Mid(Str(AccessCode[0]),RadioState[8]+1,1)
								If RadioState[6]-fps\Factor[0] =< RadioState[7]*50 And RadioState[6]>RadioState[7]*50 Then
									PlaySound_Strict(RadioBuzz)
									RadioState[7]=RadioState[7]+1
									If RadioState[7]=>temp Then
										RadioState[7]=0
										RadioState[6]=-100
										RadioState[8]=RadioState[8]+1
										If RadioState[8]=4 Then RadioState[8]=0 : RadioState[6]=-200
									EndIf
								EndIf
							Else
								
								Local mouseZspeed_var# = MouseZSpeed()
								
								If mouseZspeed_var# <> 0 Then
									PlaySound_Strict RadioSquelch
									If RadioCHN[Int(SelectedItem\state2)] <> 0 Then PauseChannel(RadioCHN[Int(SelectedItem\state2)])
									If mouseZspeed_var# > 0 Then
										SelectedItem\state2 = SelectedItem\state2 + 1
										If SelectedItem\state2 >= 5 Then
											SelectedItem\state2 = 0
										EndIf
									Else
										SelectedItem\state2 = SelectedItem\state2 - 1
										If SelectedItem\state2 < 0 Then
											SelectedItem\state2 = 4
										EndIf
									EndIf
									If RadioCHN[SelectedItem\state2]<>0 Then ResumeChannel(RadioCHN[SelectedItem\state2])
								EndIf
							EndIf
							SetFont fo\Font[Font_Default]
						EndIf
					EndIf
					;[End Block]
				Case "navigator", "nav"
					;[Block]
					If SelectedItem\state <= 100 Then SelectedItem\state = Max(0, SelectedItem\state - fps\Factor[0] * 0.005)
					
					If PlayerRoom\RoomTemplate\Name <> "pocketdimension" Then
						If SelectedItem\state > 0.0 And SelectedItem\state <= 20.0 Then
							UpdateBatteryTimer()
							If BatMsgTimer >= 70.0 * 1.0 Then
								If (Not ChannelPlaying(LowBatteryCHN[0])) Then LowBatteryCHN[0] = PlaySound_Strict(LowBatterySFX[0])
							EndIf
						EndIf
					EndIf
					;[End Block]
;! ~ Consumable - Items
				Case "red_candy","blue_candy","yellow_candy"
					;[Block]
					If (Not wbi\Helmet Lor wbi\GasMask Lor mpl\HasNTFGasmask Lor hds\Wearing) Then
						StaminaEffect = 0.9
						StaminaEffectTimer = 200
						If SelectedItem\itemtemplate\name = "red_candy" Then
							CreateMsg(GetLocalString("Items","candy_1"))
						ElseIf SelectedItem\itemtemplate\name = "blue_candy" Then
							CreateMsg(GetLocalString("Items","candy_2"))
						Else
							CreateMsg(GetLocalString("Items","candy_3"))
						EndIf
						RemoveItem(SelectedItem)
					Else
						CreateMsg(GetLocalString("Items","cant_scp1079"))
					EndIf
					;[End Block]
				Case "veryfinefirstaid"
					;[Block]
					If (Not wbi\Hazmat Lor wbi\GasMask) Then
						If (Not I_402\Using) Then
							Select Rand(5)
								Case 1
									DamageSPPlayer(80, True)
									CreateMsg(GetLocalString("Items", "strangebottle_1"))
								Case 2
									HealSPPlayer(100)
									I_1079\Foam = 0
									CreateMsg(GetLocalString("Items", "strangebottle_2"))
								Case 3
									HealSPPlayer(50)
									CreateMsg(GetLocalString("Items", "strangebottle_3"))
								Case 4
									BlurTimer = 10000
									HealSPPlayer(25)
									CreateMsg(GetLocalString("Items", "bluefirstaid_3"))
								Case 5
									BlinkTimer = -10
									Local roomname$ = PlayerRoom\RoomTemplate\Name
									Local r.Rooms
									For r.Rooms = Each Rooms
										If r\RoomTemplate\Name = "pocketdimension" Then
											PositionEntity(Collider, EntityX(r\obj),0.8,EntityZ(r\obj))		
											ResetEntity Collider									
											UpdateDoors()
											UpdateRooms()
											PlaySound_Strict(Use914SFX)
											DropSpeed = 0
											Curr106\State = -2500
											Exit
										EndIf
									Next
									CreateMsg(GetLocalString("Items", "strangebottle_4"))
							End Select
						Else
							CreateMsg(Chr(34)+GetLocalString("Singleplayer","i_cant")+Chr(34))
							Return
							SelectedItem = Null
						EndIf
						
						RemoveItem(SelectedItem)
					Else
						CreateMsg(GetLocalString("Singleplayer","cant_firstaid"))
					EndIf
					;[End Block]
				Case "firstaid", "finefirstaid", "firstaid2"
					;[Block]
					If psp\Health = 100 Then
						I_1079\Foam = 0
						CreateMsg(GetLocalString("Items", "firstaid_noneed"))
						SelectedItem = Null
					Else
						If (Not wbi\Hazmat) Then
							If (Not I_402\Using) Then
								CurrSpeed = CurveValue(0, CurrSpeed, 5.0)
								Crouch = True
								
								SelectedItem\state = Min(SelectedItem\state+(fps\Factor[0]/5.0),100)			
								
								If SelectedItem\state = 100 Then
									If SelectedItem\itemtemplate\tempname = "finefirstaid" Then
										HealSPPlayer(45)
										If psp\Health = 100 Then
											CreateMsg(GetLocalString("Items", "finefirstaid_1"))
										ElseIf psp\Health > 70 Then
											CreateMsg(GetLocalString("Items", "finefirstaid_2"))
										Else
											CreateMsg(GetLocalString("Items", "finefirstaid_3"))
										EndIf
										RemoveItem(SelectedItem)
									Else
										HealSPPlayer(30)
										CreateMsg(GetLocalString("Items", "firstaid_1"))
										
										If SelectedItem\itemtemplate\tempname = "firstaid2" Then 
											Select Rand(6)
												Case 1
													SuperMan = True
													CreateMsg(GetLocalString("Items", "bluefirstaid_1"))
												Case 2
													InvertMouse = (Not InvertMouse)
													CreateMsg(GetLocalString("Items", "bluefirstaid_2"))
												Case 3
													BlurTimer = 5000
													CreateMsg(GetLocalString("Items", "bluefirstaid_3"))
												Case 4
													BlinkEffect = 0.6
													BlinkEffectTimer = Rand(20,30)
												Case 5
													HealSPPlayer(100)
													CreateMsg(GetLocalString("Items", "bluefirstaid_4"))
												Case 6
													CreateMsg(GetLocalString("Items", "bluefirstaid_5"))
													DamageSPPlayer(70, True)
											End Select
										EndIf
										RemoveItem(SelectedItem)
									EndIf							
								EndIf
							Else
								CreateMsg(Chr(34)+GetLocalString("Singleplayer","i_cant")+Chr(34))
								Return
								SelectedItem = Null
							EndIf
						Else
							CreateMsg(GetLocalString("Singleplayer","cant_firstaid_with_hazmat"))
						EndIf
					EndIf
					;[End Block]
				Case "eyedrops","fineeyedrops","supereyedrops"
					;[Block]
					If (Not wbi\Helmet Lor wbi\GasMask Lor wbi\NightVision Lor wbi\SCRAMBLE Lor mpl\HasNTFGasmask Lor hds\Wearing) Then
						If (Not I_402\Using) Then
							If I_714\Using <> 1 Then
								If SelectedItem\itemtemplate\tempname = "eyedrops" Then
									BlinkEffect = 0.6
									BlinkEffectTimer = Rand(20,30)
									BlurTimer = 200
								ElseIf SelectedItem\itemtemplate\tempname = "fineeyedrops" Then
									BlinkEffect = 0.4
									BlinkEffectTimer = Rand(30,40)
									BlurTimer = 200
								Else
									BlinkEffect = 0.0
									BlinkEffectTimer = 60
									EyeStuck = 10000
								EndIf
							Else
								BlurTimer = 1000
							EndIf
						Else
							CreateMsg(Chr(34)+GetLocalString("Singleplayer","i_cant")+Chr(34))
							Return
							SelectedItem = Null
						EndIf
						RemoveItem(SelectedItem)
					Else
						CreateMsg(GetLocalString("Items","cant_eyedrops"))
					EndIf
					;[End Block]
				Case "cup"
					;[Block]
					If (Not wbi\Helmet Lor wbi\GasMask Lor mpl\HasNTFGasmask Lor hds\Wearing) Then
						If (Not I_402\Using) Then
							
							If IsVaneCoinDropped Then
								If opt\MusicVol > 0 Then
									PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Vane_NTF.ogg"))
									GiveAchievement(AchvVane)
								EndIf
							EndIf
							
							SelectedItem\name = Trim(Lower(SelectedItem\name))
							If Left(SelectedItem\name, Min(6,Len(SelectedItem\name))) = "cup of" Then
								SelectedItem\name = Right(SelectedItem\name, Len(SelectedItem\name)-7)
							ElseIf Left(SelectedItem\name, Min(8,Len(SelectedItem\name))) = "a cup of" 
								SelectedItem\name = Right(SelectedItem\name, Len(SelectedItem\name)-9)
							EndIf
							
							x2 = (SelectedItem\state+1.0)
							
							Local iniStr$ = Data294
							Local drink$ = FindSCP294Drink(SelectedItem\name)

							StrTemp = GetFileLocalString(iniStr, drink, "message")
							
							If IsVaneCoinDropped Then
								CreateMsg("DAMN, THAT SOUNDS NICE!",70*10,True,Rand(100,255),Rand(100,255),Rand(100,255))
							Else
								If StrTemp <> "" Then
									If StrTemp <> "jorge" Then
										CreateMsg(StrTemp)
									Else
										CreateMsg(StrTemp,70*6,True,255,216,0)
									EndIf
								EndIf
							EndIf
							
							If Integer(GetFileLocalString(iniStr, drink, "lethal")) Lor Integer(GetFileLocalString(iniStr, drink, "deathtimer")) Then 
								m_msg\DeathTxt = GetFileLocalString(iniStr, drink, "deathmessage")
								If Integer(GetFileLocalString(iniStr, drink, "lethal")) Then Kill()
							EndIf
							BlurTimer = Integer(GetFileLocalString(iniStr, drink, "blur"))*70
							If VomitTimer = 0 Then VomitTimer = Integer(GetFileLocalString(iniStr, drink, "vomit"))
							CameraShakeTimer = GetFileLocalString(iniStr, drink, "camerashake")
							DamageSPPlayer(Integer(GetFileLocalString(iniStr, drink, "damage")) * 25.0, True)
							StrTemp =  GetFileLocalString(iniStr, drink, "sound")
							If StrTemp<>"" Then
								PlaySound_Strict LoadTempSound(StrTemp)
							EndIf
							If Integer(GetFileLocalString(iniStr, drink, "stomachache")) Then I_1025\State[3]=1
							
							DeathTimer = Integer(GetFileLocalString(iniStr, drink, "deathtimer"))*70
							
							If Integer(GetFileLocalString(iniStr, drink, "cola")) Then I_207\Factor = 1
							
							I_1079\Foam = Max(I_1079\Foam + Integer(GetFileLocalString(iniStr, drink, "bubble foam")),0)
							
							BlinkEffect = Float(GetFileLocalString(iniStr, drink, "blink effect", 1.0))*x2
							BlinkEffectTimer = Float(GetFileLocalString(iniStr, drink, "blink effect timer", 1.0))*x2
							
							StaminaEffect = Float(GetFileLocalString(iniStr, drink, "stamina effect", 1.0))*x2
							StaminaEffectTimer = Float(GetFileLocalString(iniStr, drink, "stamina effect timer", 1.0))*x2
							
							StrTemp = GetFileLocalString(iniStr, drink, "refusemessage")
							If StrTemp <> "" Then
								CreateMsg(StrTemp)
							Else
								it.Items = CreateItem("Empty Cup", "emptycup", 0,0,0)
								it\Picked = True
								For i = 0 To MaxItemAmount-1
									If Inventory[i]=SelectedItem Then Inventory[i] = it : Exit
								Next					
								EntityType (it\collider, HIT_ITEM)
								
								RemoveItem(SelectedItem)						
							EndIf
							
							SelectedItem = Null
						Else
							CreateMsg(Chr(34)+GetLocalString("Singleplayer","i_cant")+Chr(34))
							Return
							SelectedItem = Null
						EndIf
					Else
						CreateMsg(GetLocalString("Items","cant_cup"))
					EndIf
					;[End Block]
				Case "syringe","finesyringe","veryfinesyringe"
					;[Block]
					If (Not wbi\Hazmat Lor hds\Wearing) Then
						If (Not I_402\Using) Then
							If SelectedItem\itemtemplate\name = "syringe" Then
								HealTimer = 30
								StaminaEffect = 0.5
								StaminaEffectTimer = 20
							ElseIf SelectedItem\itemtemplate\name = "finesyringe" Then
								HealTimer = Rnd(20, 40)
								StaminaEffect = Rnd(0.5, 0.8)
								StaminaEffectTimer = Rnd(20, 30)
							Else
								Select Rand(3)
									Case 1
										HealTimer = Rnd(40, 60)
										StaminaEffect = 0.1
										StaminaEffectTimer = 30
										CreateMsg(GetLocalString("Items", "syringe_huge"))
									Case 2
										SuperMan = True
										CreateMsg(GetLocalString("Items", "syringe_super"))
									Case 3
										VomitTimer = 30
										CreateMsg(GetLocalString("Items", "syringe_vomit"))
								End Select
							EndIf
							
							CreateMsg(GetLocalString("Items", "syringe_slight"))
							
							RemoveItem(SelectedItem)
						Else
							CreateMsg(Chr(34)+GetLocalString("Singleplayer","i_cant")+Chr(34))
							Return
							SelectedItem = Null
						EndIf
					Else
						CreateMsg(GetLocalString("Items","cant_syringe"))
					EndIf
					;[End Block]
				Case "cigarette"
					;[Block]
					If (Not wbi\Helmet Lor wbi\GasMask Lor mpl\HasNTFGasmask Lor hds\Wearing) Then
						If SelectedItem\state = 0 Then
							Select Rand(6)
								Case 1
									CreateMsg(GetLocalString("Items", "cigarette_1"))
								Case 2
									CreateMsg(GetLocalString("Items", "cigarette_2"))
								Case 3
									CreateMsg(GetLocalString("Items", "cigarette_3"))
									RemoveItem(SelectedItem)
								Case 4
									CreateMsg(GetLocalString("Items", "cigarette_4"))
								Case 5
									CreateMsg(GetLocalString("Items", "cigarette_5"))
								Case 6
									CreateMsg(GetLocalString("Items", "cigarette_6"))
									RemoveItem(SelectedItem)
							End Select
							SelectedItem\state = 1 
						Else
							CreateMsg(GetLocalString("Items", "cigarette_2"))
						EndIf
					Else
						CreateMsg(GetLocalString("Items", "cant_cigarette"))
					EndIf
					;[End Block]
				Case "pill"
					;[Block]
					If (Not wbi\Helmet Lor wbi\GasMask Lor mpl\HasNTFGasmask Lor hds\Wearing) Then
						CreateMsg(GetLocalString("Items", "pill"))
						
						RemoveItem(SelectedItem)
						SelectedItem = Null
					Else
						CreateMsg(GetLocalString("Items", "cant_pill"))
					EndIf	
					;[End Block]
				Case "beer"
					;[Block]
					If (Not wbi\GasMask Lor wbi\Helmet Lor wbi\Hazmat Lor hds\Wearing) Then
						PlaySound_Strict(LoadTempSound("SFX\SCP\294\ew"+Rand(1,2)+".ogg"))
						RemoveItem(SelectedItem)
						CreateMsg(GetLocalString("Items","beer_use"))
					Else
						CreateMsg(GetLocalString("Items","cant_beer"))
					EndIf
					SelectedItem = Null
					;[End Block]
;! ~ SCP - Items
				Case "scp016"
					PlaySound_Strict LoadTempSound("SFX\SCP\016\DishBreak.ogg")
					
					If (Not wbi\Hazmat Lor hds\Wearing) Then
						GiveAchievement(Achv016)
						CreateMsg(GetLocalString("Items", "scp016_1"))
						DamageSPPlayer(1,True)
						I_016\Timer = 10
						RemoveItem(SelectedItem)
					Else
						CreateMsg(GetLocalString("Items", "scp016_2"))
					EndIf
					;[End Block]
				Case "scp109"
					;[Block]
					If (Not wbi\Helmet Lor wbi\GasMask Lor mpl\HasNTFGasmask Lor hds\Wearing) Then
						If (Not I_402\Using) Then
							If I_109\Sound[0] = 0 Then
								I_109\Sound[0] = LoadSound_Strict("SFX\SCP\109\ahh.ogg")
							EndIf
							
							GiveAchievement(Achv109)
							
							I_109\Used = I_109\Used + 1
							I_109\Timer = 70*10
							
							Stamina = Stamina + Rnd(60)
							psp\Health = Max(0, psp\Health - Rnd(1, 2))
							
							If I_109\Used < 5 Then
								PlaySound_Strict I_109\Sound[0]
								
								If Rand(3) = 1 Then
									CreateMsg(GetLocalString("Items", "scp109_1"))
								Else
									If Rand(2) = 1 Then
										CreateMsg(GetLocalString("Items", "scp109_2"))
									Else
										CreateMsg(GetLocalString("Items", "scp109_3"))
									EndIf
								EndIf
							Else
								BlurTimer = 10000
								
								I_109\Vomit = True
								VomitTimer = -4
								
								CreateMsg(GetLocalString("Items", "scp109_vomit"))
							EndIf
							SelectedItem\state = 0
							SelectedItem = Null
						Else
							CreateMsg(Chr(34)+GetLocalString("Singleplayer","i_cant")+Chr(34))
							Return
							SelectedItem = Null
						EndIf
					Else
						CreateMsg(GetLocalString("Items","cant_scp109"))
					EndIf
					SelectedItem = Null
					;[End Block]
				Case "scp198"
					;[Block]
					PlaySound_Strict(LoadTempSound("SFX\SCP\294\ew"+Rand(1,2)+".ogg"))
					CreateMsg(GetLocalString("Items", "scp198_2"))
					If Rand(5) = 1 Then
						I_198\Vomit = True
						VomitTimer = -5
						If I_1033RU\HP = 0
							HealSPPlayer(I_198\Injuries)
						EndIf
						I_198\Injuries = 0
						I_198\DeathTimer = 1
					Else
						If I_1033RU\HP = 0
							HealSPPlayer(I_198\Injuries)
						EndIf
						I_198\Injuries = 0
						I_198\DeathTimer = 1
					EndIf
					SelectedItem = Null
					;[End Block]
				Case "scp207"
					;[Block]
					If wbi\GasMask Lor wbi\Helmet Lor wbi\Hazmat Lor hds\Wearing Then
						CreateMsg(GetLocalString("Items","cant_scp207"))
						SelectedItem = Null
						Return
					Else
						If (Not I_402\Using) Then
							PlaySound_Strict LoadTempSound("SFX\SCP\109\Ahh.ogg")
							
							I_207\Factor = I_207\Factor + 1
							
							I_207\Limit = I_207\Limit + 1
							
							Select I_207\Factor
								Case 1
									I_207\Timer = 0
									I_207\Timer = 1.0
								Case 4, 5
									InfiniteStamina = True
								Case 6
									SuperMan = True
							End Select
							
							If I_207\Factor > 1 Then
								I_207\DeathTimer = 1.0
							EndIf
							
							BlinkEffect = 0.8
							BlinkEffectTimer = 150
							StaminaEffect = 0.8
							StaminaEffectTimer = 10
							
							I_357\Timer = 0
							I_402\Timer = 0
							I_1079\Take = 0
							I_1079\Foam = 0
							I_1079\Trigger = 0
							I_016\Timer = 0
							I_059\Timer = 0
							DeathTimer = 0
							I_008\Timer = 0
							Stamina = 100
							psp\Health = 110
							For i = 0 To 5
								I_1025\State[i]=0
							Next
							
							For e.Events = Each Events
								If e\EventName = "cont_009" Then e\EventState[0] = 0.0 : e\EventState[2] = 0.0
							Next
							
							Select Rand(1, 4)
								Case 1
									CreateMsg(GetLocalString("Items", "scp207_1"))
								Case 2
									CreateMsg(GetLocalString("Items", "scp207_2"))
								Case 3
									CreateMsg(GetLocalString("Items", "scp207_3"))
								Case 4
									CreateMsg(GetLocalString("Items", "scp207_4"))
							End Select
							
							SelectedItem\state = 0
							
							If I_207\Limit >= 4 Then 
								RemoveItem(SelectedItem)
								If it = Null Then
									it = CreateItem("SCP-207 Empty Bottle","scp207empty",EntityX(Collider),EntityY(Camera,True),EntityX(Collider))
									EntityType it\collider, HIT_ITEM
								EndIf
								I_207\Limit = 0
							EndIf
						Else
							CreateMsg(Chr(34)+GetLocalString("Singleplayer","i_cant")+Chr(34))
							Return
							SelectedItem = Null
						EndIf
					EndIf
					SelectedItem = Null
					;[End Block]
				Case "scp268", "super268"
					;[Block]
					If (Not wbi\GasMask Lor wbi\Helmet Lor wbi\Hazmat Lor wbi\NightVision Lor wbi\SCRAMBLE Lor hds\Wearing) Then
						If I_268\Using > 0 Then
							CreateMsg(GetLocalString("Items", "scp268_off"))
							If ChannelPlaying(I_268\SoundCHN[0]) = False Then PlaySound_Strict I_268\Sound[1]
							I_268\Using = False
						Else
							GiveAchievement(Achv268)
							
							PlaySound_Strict I_268\Sound[0]
							CreateMsg(GetLocalString("Items", "scp268_on"))
							PlaySound_Strict(LoadTempSound("SFX\SCP\268\InvisibilityOn.ogg"))
							If SelectedItem\itemtemplate\tempname = "scp268"
								I_268\Using = 1
							Else
								I_268\Using = 2
							EndIf
						EndIf
					Else
						CreateMsg(GetLocalString("Items","cant_scp268"))
					EndIf
					SelectedItem = Null
					;[End Block]
				Case "scp402"
					;[Block]
					If I_402\Timer >= 40
						I_402\Using = 1
						CreateMsg(GetLocalString("Items","scp402_1"))
						SelectedItem = Null
						Return
					EndIf
					
					If (Not hds\Wearing Lor wbi\Hazmat Lor wbi\GasMask Lor mpl\HasNTFGasmask) Then
						If I_402\Using = 1 Then
							CreateMsg(GetLocalString("Items","scp402_off"))
							I_402\Using = 0
						Else
							GiveAchievement(Achv402)
							CreateMsg(GetLocalString("Items","scp402_on"))
							I_402\Using = 1
						EndIf
						SelectedItem = Null
					Else
						CreateMsg(GetLocalString("Singleplayer","cant_scp402"))
						SelectedItem = Null
					EndIf
					;[End Block]
				Case "420"
					;[Block]
					If (Not wbi\Helmet Lor wbi\GasMask Lor mpl\HasNTFGasmask Lor hds\Wearing) Then
						If I_714\Using=1 Then
							CreateMsg(Chr(34) + GetLocalString("Items", "scp420_no") + Chr(34))
						Else
							CreateMsg(Chr(34) + GetLocalString("Items", "scp420_1") + Chr(34))
							HealSPPlayer(80)
							BlurTimer = 500
							GiveAchievement(Achv420)
							If opt\MusicVol > 0 Then
								PlaySound_Strict LoadTempSound("SFX\Music\SCPs\420-J_"+Rand(1,2)+".ogg")
								EndIf
						EndIf
						RemoveItem(SelectedItem)
					Else
						CreateMsg(GetLocalString("Items", "cant_scp420"))
					EndIf
					;[End Block]
				Case "420s"
					;[Block]
					If (Not wbi\Helmet Lor wbi\GasMask Lor mpl\HasNTFGasmask Lor hds\Wearing) Then
						If I_714\Using=1 Then
							CreateMsg(Chr(34) + GetLocalString("Items", "scp420_no") + Chr(34))
						Else
							m_msg\DeathTxt = GetLocalStringR("Singleplayer", "scp420_death_1",Designation)
							m_msg\DeathTxt = m_msg\DeathTxt+GetLocalString("Singleplayer", "scp420_death_2")
							m_msg\DeathTxt = m_msg\DeathTxt+GetLocalString("Singleplayer", "scp420_death_3")
							CreateMsg(Chr(34) + GetLocalString("Items", "scp420_2") + Chr(34))
							KillTimer = -1						
						EndIf
						RemoveItem(SelectedItem)
					Else
						CreateMsg(GetLocalString("Items", "cant_scp420"))
					EndIf
					;[End Block]
				Case "scp427"
					;[Block]
					If I_427\Using > 0 Then
						CreateMsg(GetLocalString("Items", "scp427_off"))
						I_427\Using = False
					Else
						GiveAchievement(Achv427)
						CreateMsg(GetLocalString("Items", "scp427_on"))
						I_427\Using = True
					EndIf
					SelectedItem = Null
					;[End Block]
				Case "scp500"
					;[Block]					
					If ItemAmount < MaxItemAmount Then
						For n% = 0 To ItemAmount+0
							If Inventory[n] = Null Then
								If ItemAmount > MaxItemAmount Then
									CreateMsg(GetLocalString("Items", "cannot_carry"))
								Else
									Inventory[n] = CreateItem("SCP-500-01", "scp500pill", 1, 1, 1)
									Inventory[n]\Picked = True
									Inventory[n]\Dropped = -1
									Inventory[n]\itemtemplate\found=True
									I_500\Limit = I_500\Limit + 1
									HideEntity Inventory[n]\collider
									EntityType (Inventory[n]\collider, HIT_ITEM)
									EntityParent(Inventory[n]\collider, 0)
									CreateMsg(GetLocalString("Items", "scp500_take"))
								EndIf										
							EndIf	
						Next
						If I_500\Limit >= 3 Then 
							RemoveItem(SelectedItem)
							I_500\Limit = 0
						EndIf
					Else
						CreateMsg(GetLocalString("Items", "cannot_carry"))				
					EndIf																																										
					;[End Block]
				Case "scp500pill"
					;[Block]
					If (Not wbi\Helmet Lor wbi\GasMask Lor mpl\HasNTFGasmask Lor hds\Wearing) Then
						If psp\Health < 100 And I_008\Timer > 0 Then
							CreateMsg(GetLocalString("Items", "scp500_1"))
						ElseIf I_008\Timer > 0 Then
							CreateMsg(GetLocalString("Items", "scp500_2"))
						ElseIf I_409\Timer > 0.0 Then
							CreateMsg(GetLocalString("Items", "scp500_5"))
							I_409\Revert = True
						Else
							CreateMsg(GetLocalString("Items", "scp500_3"))
						EndIf
						If I_357\Timer > 0 Then
							CreateMsg(GetLocalString("Items", "scp500_4"))
						EndIf
						
						GiveAchievement(Achv500)
						
						DeathTimer = 0
						HealSPPlayer(100)
						Sanity = 0
						
						I_008\Timer = 0
						I_016\Timer = 0
						I_059\Timer = 0
						I_109\Timer = 0
						I_109\Used = 0
						I_109\Vomit = False
						I_109\VomitTimer = 0
						I_198\Timer = 0
						I_198\DeathTimer = 0
						I_198\Injuries = 0
						I_198\Vomit = False
						I_198\VomitTimer = 0
						I_207\Timer = 0
						I_207\DeathTimer = 0
						I_207\Factor = 0
						I_357\Timer = 0
						I_402\Timer = 0
						I_409\Timer = 0
						I_1079\Foam = 0
						I_1079\Trigger = 0
						
						Stamina = 100
						For i = 0 To 5
							I_1025\State[i]=0
						Next
						
						RemoveItem(SelectedItem)
						SelectedItem = Null
					Else
						CreateMsg(GetLocalString("Items","cant_pill"))
					EndIf
					;[End Block]
				Case "scp500death"
					;[Block]
					If (Not wbi\Helmet Lor wbi\GasMask Lor mpl\HasNTFGasmask Lor hds\Wearing) Then
						CreateMsg(GetLocalString("Items", "pill"))
						
						If I_427\Timer < 70*360 Then
							I_427\Timer = 70*360
						EndIf
						
						RemoveItem(SelectedItem)
						SelectedItem = Null
					Else
						CreateMsg(GetLocalString("Items", "cant_pill"))
					EndIf
					;[End Block]
				Case "scp513"
					;[Block]
					PlaySound_Strict LoadTempSound("SFX\SCP\513\Bell1.ogg")
					
					If Curr5131 = Null
						Curr5131 = CreateNPC(NPCtype5131, 0,0,0)
					EndIf	
					SelectedItem = Null
					;[End Block]
				Case "scp714"
					;[Block]
					If I_714\Using=1 Then
						CreateMsg(GetLocalString("Items", "scp714_off"))
						I_714\Using = False
					Else
						GiveAchievement(Achv714)
						CreateMsg(GetLocalString("Items", "scp714_on"))
						I_714\Using = True
					EndIf
					SelectedItem = Null	
					;[End Block]
				Case "scp1025"
					;[Block]
					GiveAchievement(Achv1025) 
					If SelectedItem\itemtemplate\img=0 Then
						SelectedItem\state = Rand(0,5)
					EndIf
					
					If (Not I_714\Using) Then I_1025\State[SelectedItem\state]=Max(1,I_1025\State[SelectedItem\state])
					;[End Block]
				Case "scp1033ru", "super1033ru"
					;[Block]
					If I_1033RU\Using > 0 Then
						CreateMsg(GetLocalString("Items", "scp1033ru_off"))
						I_1033RU\Using = False
					Else
						GiveAchievement(Achv1033RU)
						CreateMsg(GetLocalString("Items", "scp1033ru_on"))
						If SelectedItem\itemtemplate\tempname = "scp1033ru" Then
							I_1033RU\Using = 1
						Else
							I_1033RU\Using = 2
						EndIf
					EndIf
					SelectedItem = Null		
					;[End Block]
				Case "scp1079"
					;[Block]					
					If ItemAmount < MaxItemAmount Then
						For n% = 0 To ItemAmount
							If Inventory[n] = Null Then
								If ItemAmount > MaxItemAmount Then
									CreateMsg(GetLocalString("Items", "cannot_carry"))
								Else
									Inventory[n] = CreateItem("SCP-1079-01", "scp1079sweet", 1, 1, 1)
									Inventory[n]\Picked = True
									Inventory[n]\Dropped = -1
									Inventory[n]\itemtemplate\found=True
									I_1079\Limit = I_1079\Limit + 1
									HideEntity Inventory[n]\collider
									EntityType (Inventory[n]\collider, HIT_ITEM)
									EntityParent(Inventory[n]\collider, 0)
									CreateMsg(GetLocalString("Items", "scp1079_took"))
								EndIf										
							EndIf	
						Next
						If I_1079\Limit >= 4 Then 
							RemoveItem(SelectedItem)
							I_1079\Limit = 0
						EndIf
					Else
						CreateMsg(GetLocalString("Items", "cannot_carry"))
					EndIf																																									
					;[End Block]
				Case "scp1079sweet"
					;[Block]
					If (Not wbi\Helmet Lor wbi\GasMask Lor mpl\HasNTFGasmask Lor hds\Wearing) Then
						CreateMsg(GetLocalString("Items", "scp1079_1"))
						
						I_1079\Take = I_1079\Take + 1
						If I_1033RU\HP = 0
							DamageSPPlayer(Rand(20,30),True)
						Else
							Damage1033RU(Rand(5))
						EndIf
						
						PlaySound_Strict(SizzSFX(Rand(0,1)))
						
						GiveAchievement(Achv1079)
						
						Local tempchn%
						
						RemoveItem(SelectedItem)
						pvt = CreatePivot()
						PositionEntity pvt, EntityX(Collider)+Rnd(-0.05,0.05),EntityY(Collider)-0.05,EntityZ(Collider)+Rnd(-0.05,0.05)
						TurnEntity pvt, 90, 0, 0
						EntityPick(pvt,0.3)
						Local de.Decals = CreateDecal(DECAL_1079, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
						de\Size = Rnd(0.1,0.2) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
						de.Decals = CreateDecal(DECAL_1079, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
						de\Size = Rnd(0.06,0.1) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
						de.Decals = CreateDecal(DECAL_1079, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
						de\Size = Rnd(0.2,0.25) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
						de.Decals = CreateDecal(DECAL_1079, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
						de\Size = Rnd(0.3,0.31) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
						ChannelVolume tempchn, Rnd(0.0,0.8)*opt\SFXVolume
						ChannelPitch tempchn, Rand(20000,30000)
						
						FreeEntity pvt
					Else
						CreateMsg(GetLocalString("Items", "cant_scp1079"))
					EndIf
					
					If I_1079\Take >= 4 Then
						I_1079\Trigger = 1													
						Kill()
						m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp1079_death_1",Designation)
						m_msg\DeathTxt = m_msg\DeathTxt+GetLocalString("Singleplayer","scp1079_death_2")
						m_msg\DeathTxt = m_msg\DeathTxt+GetLocalString("Singleplayer","scp1079_death_3")
					EndIf
					;[End Block]
				Case "scp1102ru"
					;[Block]
					If (Not IsPlayerOutside()) Then
						CurrSpeed = CurveValue(0.0, CurrSpeed, 5.0)
						
						SelectedItem\state = Min(SelectedItem\state + (fps\Factor[0]/2), 100.0)
						
						If SelectedItem\state = 100.0 Then
							If I_1102RU\IsInside = 0 Then
								If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
								CreateMsg(GetLocalString("Items", "scp1102ru_1"))
								I_1102RU\IsInside = 1
								PlaySound_Strict I_1102RU\Sound[0]
								
								GiveAchievement(Achv1102RU)
								
								For r = Each Rooms
									If r\RoomTemplate\Name = "area_1102_ru" Then
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
								
							EndIf
							
							RemoveItem(SelectedItem)
						EndIf
					Else
						CreateMsg(GetLocalString("Items", "scp1102ru_jammed"))
					EndIf
					;[End Block]
				Default
					;[Block]
					If SelectedItem\invSlots>0 Then
						DoubleClick = 0
						MouseHit1 = 0
						MouseDown1 = 0
						LastMouseHit1 = 0
						OtherOpen = SelectedItem
						SelectedItem = Null
					EndIf
					;[End Block]
			End Select
			
			If SelectedItem <> Null Then
				If SelectedItem\itemtemplate\img <> 0
					Local IN$ = SelectedItem\itemtemplate\tempname
					If IN$ = "paper" Lor IN$ = "badge" Lor IN$ = "oldpaper" Lor IN$ = "ticket" Then
						Local a_it.Items
						For a_it.Items = Each Items
							If a_it <> SelectedItem
								Local IN2$ = a_it\itemtemplate\tempname
								If IN2$ = "paper" Lor IN2$ = "badge" Lor IN2$ = "oldpaper" Lor IN2$ = "ticket" Then
									If a_it\itemtemplate\img<>0
										If a_it\itemtemplate\img <> SelectedItem\itemtemplate\img
											FreeImage(a_it\itemtemplate\img)
											a_it\itemtemplate\img = 0
										EndIf
									EndIf
								EndIf
							EndIf
						Next
					EndIf
				EndIf			
			EndIf
			
			If MouseHit2 Then
				EntityAlpha Overlay[10], 0.0
				
				IN$ = SelectedItem\itemtemplate\tempname
				If IN$ = "scp1025" Then
					If SelectedItem\itemtemplate\img<>0 Then FreeImage(SelectedItem\itemtemplate\img)
					SelectedItem\itemtemplate\img=0
				EndIf
				If IN$="nvgogles" Lor IN$="supernv" Lor IN$="finenvgogles" Lor IN$ = "scramble" Lor IN$ = "veryfinescramble" Then
					SelectedItem\state2 = 0
				EndIf
				If IN$="hazmatsuit" Lor IN$="hazmatsuit2" Lor IN$="hazmatsuit3" Lor IN$="ntf_helmet" Lor IN$="fine_ntf_helmet" Lor IN$="helmet" Lor IN$="finehelmet" Lor IN$="gasmask" Lor IN$="supergasmask" Lor IN$="gasmask3"
					SelectedItem\state = 0
				EndIf
				If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
				SelectedItem = Null
			EndIf
		End If		
	EndIf
	
	If SelectedItem = Null Then
		For i = 0 To 6
			If RadioCHN[i] <> 0 Then
				If ChannelPlaying(RadioCHN[i]) Then PauseChannel(RadioCHN[i])
			EndIf
		Next
	EndIf
	
	For it.Items = Each Items
		If it <> SelectedItem Then
			Select it\itemtemplate\tempname
				Case "hazmatsuit","hazmatsuit2","hazmatsuit3","ntf_helmet","fine_ntf_helmet","helmet","finehelmet","gasmask","supergasmask","gasmask3"
					it\state = 0.0
				Case "nvgoggles","supernv","finenvgoggles","scramble","veryfinescramble"
					it\state2 = 0.0
			End Select
		EndIf
	Next
	
	UpdateTasks()
	
	If PrevInvOpen And (Not InvOpen) Then MoveMouse Viewport_Center_X, Viewport_Center_Y
	
	CatchErrors("UpdateGUI")
End Function

Function DrawGUI()
	CatchErrors("Uncaught (DrawGUI)")
	
	Local temp%, x%, y%, z%, i%, yawvalue#, pitchvalue#
	Local x2#,y2#,z2#
	Local n%, xtemp, ytemp, strtemp$,d.Doors
	
	Local e.Events, it.Items, npc.NPCs
	
	If MenuOpen Lor ConsoleOpen Lor AttachmentOpen Lor d_I\SelectedDoor <> Null Lor InvOpen Lor OtherOpen<>Null Lor EndingTimer < 0 Then
		ShowPointer()
	Else
		HidePointer()
	EndIf
	
	If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then
		For e.Events = Each Events
			If e\room = PlayerRoom And e\EventState[0] > 600 Then
				If BlinkTimer < -3 And BlinkTimer > -11 Then
					If e\img = 0 Then
						If BlinkTimer > -5 And Rand(30)=1 Then
							If Rand(5)<5 Then PlaySound_Strict DripSFX[0]
							If e\img = 0 Then e\img = LoadImage_Strict("GFX\npcs\SCPs\106\106_face.jpg")
						EndIf
					Else
						DrawImage e\img, opt\GraphicWidth/2-Rand(390,310), opt\GraphicHeight/2-Rand(290,310)
					EndIf
				Else
					If e\img <> 0 Then FreeImage e\img : e\img = 0
				EndIf
				
				Exit
			EndIf
		Next
	EndIf
	
	If d_I\ClosestButton <> 0 And I_330\Taken < 3 And d_I\SelectedDoor = Null And InvOpen = False And MenuOpen = False And OtherOpen = Null And (Not PlayerInNewElevator)
		temp% = CreatePivot()
		PositionEntity temp, EntityX(Camera), EntityY(Camera), EntityZ(Camera)
		PointEntity temp, d_I\ClosestButton
		yawvalue# = WrapAngle(EntityYaw(Camera) - EntityYaw(temp))
		If yawvalue > 90 And yawvalue <= 180 Then yawvalue = 90
		If yawvalue > 180 And yawvalue < 270 Then yawvalue = 270
		pitchvalue# = WrapAngle(EntityPitch(Camera) - EntityPitch(temp))
		If pitchvalue > 90 And pitchvalue <= 180 Then pitchvalue = 90
		If pitchvalue > 180 And pitchvalue < 270 Then pitchvalue = 270
		
		FreeEntity (temp)
		
		If HUDenabled And psp\IsShowingHUD Then
			If d_I\ClosestButton <> 0 And d_I\ClosestDoor = Null Then
				DrawImage(PlayerIcons[3], opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32)
			EndIf
			
			If d_I\ClosestDoor <> Null Then 
				If d_I\ClosestDoor\KeyCard > 0 Then
					DrawImage(PlayerIcons[5], opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32)
				ElseIf d_I\ClosestDoor\KeyCard < 0 Then
					DrawImage(PlayerIcons[7], opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32)
				Else
					DrawImage(PlayerIcons[3], opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32)
				EndIf
			EndIf
		EndIf
	EndIf
	
	If HUDenabled And psp\IsShowingHUD Then
		If ClosestItem <> Null And I_330\Taken < 3 Then
			yawvalue# = -DeltaYaw(Camera, ClosestItem\collider)
			If yawvalue > 90 And yawvalue <= 180 Then yawvalue = 90
			If yawvalue > 180 And yawvalue < 270 Then yawvalue = 270
			pitchvalue# = -DeltaPitch(Camera, ClosestItem\collider)
			If pitchvalue > 90 And pitchvalue <= 180 Then pitchvalue = 90
			If pitchvalue > 180 And pitchvalue < 270 Then pitchvalue = 270
			
			DrawImage(PlayerIcons[4], opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32)
		EndIf
	EndIf
	
	If DrawHandIcon And HUDenabled And psp\IsShowingHUD Then DrawImage(PlayerIcons[6], opt\GraphicWidth / 2 - 32, opt\GraphicHeight / 2 - 32)
	
	For i = 0 To 3
		If DrawArrowIcon[i] Then
			x = opt\GraphicWidth / 2 - 32
			y = opt\GraphicHeight / 2 - 32		
			Select i
				Case 0
					y = y - 64 - 5
				Case 1
					x = x + 64 + 5
				Case 2
					y = y + 64 + 5
				Case 3
					x = x - 5 - 64
			End Select
			If HUDenabled And psp\IsShowingHUD Then
				DrawImage(PlayerIcons[6], x, y)
				Color 0, 0, 0
				Rect(x + 4, y + 4, 64 - 8, 64 - 8)
				DrawImage(I_MIG\ArrowIMG[i], x + 21, y + 21)
			EndIf
		EndIf
	Next
	
	If Using294 Then Use294()
	
	If (Not MenuOpen) And (Not InvOpen) And (OtherOpen=Null) And (ConsoleOpen=False) And (Using294=False) And (SelectedScreen=Null) And EndingTimer=>0 And KillTimer >= 0
		If PlayerRoom\RoomTemplate\Name$ <> "room1_intro"
		EndIf
	EndIf
	
	DrawSplashTexts()
	DrawSplashMsg()
	
	If HUDenabled Then
		
		If DebugHUD Then
			DrawDebugHUD()
		EndIf
		
		Local g.Guns
		; ~ Attachments
		DrawAttachmentsInHUD()
		
		If psp\IsShowingHUD Then
			
			Local width% = 204, height% = 20
			x% = 80
			y% = opt\GraphicHeight - 95
			; ~ Blinking Bar
			Color 255, 255, 255	
			Rect (x, y, width, height, False)
			If BlinkTimer < 150 Then
				Color 110, 0, 0
			Else
				Color 110, 110, 110
			EndIf		
			Rect(x + 3, y + 3, Float(BlinkTimer * ((width - 6) / BLINKFREQ)), 14)
			
			Color 0, 0, 0
			Rect(x - 50, y, 30, 30)
			
			If EyeIrritation > 0 And (Not wbi\NightVision) And (Not wbi\SCRAMBLE)
				Color 200, 0, 0
				Rect(x - 50 - 3, y - 3, 30 + 6, 30 + 6)
			EndIf
			
			If BlinkEffect < 1.0 Then
				Color 0, 255, 0
			Else
				Color 255, 255, 255
			EndIf
			Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
			
			DrawImage PlayerIcons[1], x - 50, y
			
			SetFont fo\Font%[Font_Digital_Medium]
			y = opt\GraphicHeight - 55
			; ~ Health
			Color 0,0,0
			Rect(x - 50, y, 30, 30)
			
			If psp\Health > 20 Then
				Color 255,255,255
			Else
				Color 255,0,0
			EndIf
			Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
			DrawImage mpl\HealthIcon, x - 50, y
			
			If psp\Health > 20
				Color 0,255,0
			Else
				Color 255,0,0
			EndIf
			TextWithAlign x + 30, y + 5, Int(psp\Health), 2
			; ~ Helmet
			If wbi\Helmet Then
				Color 0,0,0
				Rect(x +130, y, 30, 30)
				
				If psp\Helmet > 20 Then
					Color 255,255,255
				Else
					Color 255,0,0
				EndIf
				Rect(x +130 - 1, y - 1, 30 + 2, 30 + 2, False)
				
				DrawImage mpl\HelmetIcon, x +130, y
				
				If psp\Helmet > 20
					Color 0,255,0
				Else
					Color 255,0,0
				EndIf
				TextWithAlign x + 210, y + 5, Int(psp\Helmet), 2
			ElseIf mpl\HasNTFGasmask Then
				Color 0,0,0
				Rect(x +130, y, 30, 30)
				
				If psp\Helmet > 20 Then
					Color 255,255,255
				Else
					Color 255,0,0
				EndIf
				Rect(x +130 - 1, y - 1, 30 + 2, 30 + 2, False)
				
				DrawImage PlayerIcons[8], x +130, y
				
				If psp\Helmet > 20
					Color 0,255,0
				Else
					Color 255,0,0
				EndIf
				TextWithAlign x + 210, y + 5, Int(psp\Helmet), 2
			EndIf
			; ~ Armor
			If wbi\Vest Then
				Color 0,0,0
				Rect(x+40, y, 30, 30)
				
				If psp\Kevlar > 20 Then
					Color 255,255,255
				Else
					Color 255,0,0
				EndIf
				Rect(x+40 - 1, y - 1, 30 + 2, 30 + 2, False)
				If wbi\Vest = 1 Then
					DrawImage FineKevlarIcon, x+40, y
				Else
					DrawImage mpl\KevlarIcon, x+40, y
				EndIf
				
				If psp\Kevlar > 20
					Color 0,255,0
				Else
					Color 255,0,0
				EndIf
				TextWithAlign x + 120, y + 5, Int(psp\Kevlar), 2
			EndIf
			If hds\Wearing Then
				Color 0,0,0
				Rect(x+40 , y, 30, 30)
				
				If hds\Health > 20 Then
					Color 255,255,255
				Else
					Color 255,0,0
				EndIf
				Rect(x+40  - 1, y - 1, 30 + 2, 30 + 2, False)
				DrawImage mpl\HDSIcon, x+40 , y
				
				If hds\Health > 20
					Color 0,255,0
				Else
					Color 255,0,0
				EndIf
				TextWithAlign x + 120, y + 5, Int(hds\Health), 2
			EndIf
			; ~ Stamina Bar
			If Stamina < 100.0 And PlayerRoom\RoomTemplate\Name <> "pocketdimension" Then
				y = opt\GraphicHeight - 55
				x = (opt\GraphicWidth / 2) - (width / 2) + 20
				If Stamina < 30.0 Then
					Color 55, 0, 0
				Else
					Color 255, 255, 255
				EndIf
				Rect (x, y, width, height, False)
				If Stamina < 30 Then
					Color 55, 0, 0
				Else
					Color 55, 55, 55
				EndIf		
				Rect(x + 3, y + 3, Float(Stamina * (width - 6) / 100), 14)
				
				Color 0, 0, 0
				Rect(x - 50, y, 30, 30)
				
				If StaminaEffect < 1.0 Then
					Color 0, 255, 0
				Else
					If Stamina <= 0.0 Then
						Color 255, 0, 0
					Else
						Color 255, 255, 255
					EndIf
				EndIf
				Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
				If Crouch Then
					DrawImage PlayerIcons[2], x - 50, y
				Else
					DrawImage PlayerIcons[0], x - 50, y
				EndIf
			EndIf
			; ~ SCP-1033-RU
			If I_1033RU\Using > 0 Then
				If I_268\Using > 0 Then
					y% = opt\GraphicHeight - 175
				Else
					y% = opt\GraphicHeight - 135
				EndIf
				
				x% = 80
				
				Color 255, 255, 255
				Rect (x, y, width, height, False)
				
				If I_1033RU\Using = 1 Then
					If I_1033RU\HP < 35 Then
						Color 110, 0, 0
					Else
						Color 110, 110, 110
					EndIf		
					Rect(x + 3, y + 3, Float(I_1033RU\HP * (width - 6) / 100), 14)	
				Else
					If I_1033RU\HP < 75 Then
						Color 110, 0, 0
					Else
						Color 110, 110, 110
					EndIf		
					Rect(x + 3, y + 3, Float(I_1033RU\HP * (width - 6) / 200), 14)	
				EndIf
				
				Color 0, 0, 0
				Rect(x - 50, y, 30, 30)
				
				If I_1033RU\Using = 2 Then
					Color 0, 200, 0
					Rect(x - 50 - 3, y - 3, 30 + 6, 30 + 6)
					Color 0, 0, 0
					Rect(x - 50, y, 30, 30)
				EndIf
				
				Color 255, 255, 255
				Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
				
				DrawImage SCPIcons[3], x - 50, y
			EndIf
			; ~ SCP-268
			If I_268\Using > 0 Then
				y% = opt\GraphicHeight - 135
				x% = 80
				
				Color 255, 255, 255
				Rect (x, y, width, height, False)
				
				If I_268\Timer < 200 Then
					Color 110, 0, 0
				Else
					Color 110, 110, 110
				EndIf	
				Rect(x + 3, y + 3, Float(I_268\Timer * (width - 6) / 700), 14)	
				
				Color 0, 0, 0
				Rect(x - 50, y, 30, 30)
				
				If I_268\Using = 2 Then
					Color 0, 200, 0
					Rect(x - 50 - 3, y - 3, 30 + 6, 30 + 6)
					Color 0, 0, 0
					Rect(x - 50, y, 30, 30)
				EndIf 
				
				Color 255, 255, 255
				Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
				
				DrawImage SCPIcons[2], x - 50, y
			EndIf
			; ~ SCP-207
			If I_207\DeathTimer > 0 Then
				y = opt\GraphicHeight - 135
				x = (opt\GraphicWidth / 2) - (width / 2) + 20
				Color 255,255,255
				Rect (x, y, width, height, False)
				
				Color 55, 0, 55
				Rect(x + 3, y + 3, Min(Float(I_207\DeathTimer * (width - 6) / (70*420)),width - 6), 14)
				
				Color 0, 0, 0
				Rect(x - 50, y, 30, 30)
				
				Color 255, 255, 255
				Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
				DrawImage SCPIcons[1], x - 50, y
			EndIf
			; ~ SCP-198
			If I_198\Timer > 0 Then
				y = opt\GraphicHeight - 175
				x = (opt\GraphicWidth / 2) - (width / 2) + 20
				Color 255,255,255
				Rect (x, y, width, height, False)
				
				Color 255, 255, 255
				Rect(x + 3, y + 3, Min(Float(I_198\DeathTimer * (width - 6) / (70*120)),width - 6), 14)
				
				Color 0, 0, 0
				Rect(x - 50, y, 30, 30)
				
				Color 255, 255, 255
				Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
				DrawImage SCPIcons[0], x - 50, y
			EndIf
			; ~ Boss HP bar
			For npc = Each NPCs
				If npc\Boss <> Null Then
					If npc\Boss\HP > 0 Then
						x = (opt\GraphicWidth / 2) - 202
						y = 50
						width = 404
						height = 20
						
						Color 255,255,255
						Rect(x, y, width, height, False)
						
						Color 255,0,0
						SetFont fo\Font[Font_Digital_Medium]
						Text opt\GraphicWidth/2,15,npc\Boss\BossName,True,False
						Color 255,255,255
						
						If npc\Boss\HP =< npc\MaxBossHealth/6 Then
							Color 255, 0, 0
						ElseIf npc\Boss\HP =< npc\MaxBossHealth/4 Then
							Color 255, 106, 0
						ElseIf npc\Boss\HP =< npc\MaxBossHealth/2 Then
							Color 255, 216, 0
						Else
							Color 0, 127, 14
						EndIf
						Rect(x + 2, y + 3, Int(((width - 2) * (npc\Boss\HP / Float(npc\MaxBossHealth)))), 14)
					EndIf
				EndIf
			Next
			; ~ Weapons
			DrawGunsInHUD()
			
		EndIf
	EndIf
	
	If SelectedScreen <> Null Then
		DrawImage SelectedScreen\img, opt\GraphicWidth/2-ImageWidth(SelectedScreen\img)/2,opt\GraphicHeight/2-ImageHeight(SelectedScreen\img)/2
	EndIf
	
	Local PrevInvOpen% = InvOpen, MouseSlot% = 66
	Local shouldDrawHUD%=True
	
	If d_I\SelectedDoor <> Null Then
		If SelectedItem <> Null Then
			If SelectedItem\itemtemplate\tempname = "scp005" Then
				shouldDrawHUD = False
			EndIf
		EndIf
		If shouldDrawHUD Then
			If d_I\SelectedDoor\dir<>5 Then
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)+MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				Local projY# = ProjectedY()
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)-MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				Local scale# = (ProjectedY()-projY)/462.0
				
				x = opt\GraphicWidth/2-317*scale/2
				y = opt\GraphicHeight/2-462*scale/2
				
				SetFont fo\Font[Font_Digital_Small]
				If KeypadMSG <> "" Then 
					If (KeypadTimer Mod 70) < 35 Then Text opt\GraphicWidth/2, y+124*scale, KeypadMSG, True,True
				Else
					Text opt\GraphicWidth/2, y+70*scale, GetLocalString("Devices","access_code"),True,True	
					SetFont fo\Font[Font_Digital_Large]
					Text opt\GraphicWidth/2, y+124*scale, KeypadInput,True,True	
				EndIf
				
				x = x+44*scale
				y = y+249*scale
				
			Else
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)+MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				projY# = ProjectedY()
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)-MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				scale# = (ProjectedY()-projY)/462.0
				
				x = opt\GraphicWidth/2-317*scale/2
				y = opt\GraphicHeight/2-462*scale/2
				
				Color 255,0,0
				x=x+120*scale
				y=y+259*scale
				If (Not co\Enabled)
					If RectsOverlap(x,y,82*scale,82*scale,MouseX(),MouseY(),0,0)
						Rect x,y,82*scale,82*scale,False
					EndIf
				Else
					If co\KeyPad_CurrButton = 0
						Rect x,y,82*scale,82*scale,False
					EndIf
				EndIf
				
				y=y+131*scale
				If (Not co\Enabled)
					If RectsOverlap(x,y,82*scale,82*scale,MouseX(),MouseY(),0,0)
						Rect x,y,82*scale,82*scale,False
					EndIf
				Else
					If co\KeyPad_CurrButton = 1
						Rect x,y,82*scale,82*scale,False
					EndIf
				EndIf
				
				y=y+130*scale
				If (Not co\Enabled)
					If RectsOverlap(x,y,82*scale,82*scale,MouseX(),MouseY(),0,0)
						Rect x,y,82*scale,82*scale,False
					EndIf
				Else
					If co\KeyPad_CurrButton = 2
						Rect x,y,82*scale,82*scale,False
					EndIf
				EndIf
			EndIf
			
		EndIf
	EndIf
	
	Local spacing%
	Local PrevOtherOpen.Items
	
	Local OtherSize%,OtherAmount%
	
	Local isEmpty%
	
	Local isMouseOn%
	
	Local closedInv%
	
	If OtherOpen<>Null Then
		;[Block]
		PrevOtherOpen = OtherOpen
		OtherSize=OtherOpen\invSlots
		
		For i%=0 To OtherSize-1
			If OtherOpen\SecondInv[i] <> Null Then
				OtherAmount = OtherAmount+1
			EndIf
		Next
		
		Local tempX% = 0
		
		width = 70
		height = 70
		spacing% = 35
		
		x = opt\GraphicWidth / 2 - (width * MaxItemAmount /2 + spacing * (MaxItemAmount / 2 - 1)) / 2
		y = opt\GraphicHeight / 2 - (height * OtherSize /5 + height * (OtherSize / 5 - 1)) / 2
		
		For  n% = 0 To OtherSize - 1
			isMouseOn% = False
			If ScaledMouseX() > x And ScaledMouseX() < x + width Then
				If ScaledMouseY() > y And ScaledMouseY() < y + height Then
					isMouseOn = True
				EndIf
			EndIf
			
			If isMouseOn Then
				MouseSlot = n
				Color 255, 0, 0
				Rect(x - 1, y - 1, width + 2, height + 2)
			EndIf
			
			DrawFrame(x, y, width, height, (x Mod 64), (x Mod 64))
			
			If OtherOpen = Null Then Exit
			
			If OtherOpen\SecondInv[n] <> Null Then
				If (SelectedItem <> OtherOpen\SecondInv[n] Lor isMouseOn) Then DrawImage(OtherOpen\SecondInv[n]\invimg, x + width / 2 - 32, y + height / 2 - 32)
			EndIf
			If OtherOpen\SecondInv[n] <> Null And SelectedItem <> OtherOpen\SecondInv[n] Then
				If isMouseOn Then
					Color 255, 255, 255	
					Text(x + width / 2, y + height + spacing - 15, OtherOpen\SecondInv[n]\itemtemplate\name, True)
				EndIf
			Else
				
			EndIf					
			
			x=x+width + spacing
			tempX=tempX + 1
			If tempX = 5 Then 
				tempX=0
				y = y + height*2 
				x = opt\GraphicWidth / 2 - (width * MaxItemAmount /2 + spacing * (MaxItemAmount / 2 - 1)) / 2
			EndIf
		Next
		
		If SelectedItem <> Null Then
			If MouseDown1 Then
				If MouseSlot = 66 Then
					DrawImage(SelectedItem\invimg, ScaledMouseX() - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, ScaledMouseY() - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
				ElseIf SelectedItem <> PrevOtherOpen\SecondInv[MouseSlot]
					DrawImage(SelectedItem\invimg, ScaledMouseX() - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, ScaledMouseY() - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
				EndIf
			Else
				
			EndIf
		EndIf
		
		;[End Block]
		
	ElseIf InvOpen Then
		
		d_I\SelectedDoor = Null
		
		width% = 70
		height% = 70
		spacing% = 35
		
		x = opt\GraphicWidth / 2 - (width * MaxItemAmount /2 + spacing * (MaxItemAmount / 2 - 1)) / 2
		y = opt\GraphicHeight / 2 - (height * MaxItemAmount /5 + height * (MaxItemAmount / 5 - 1)) / 2
		
		For  n% = 0 To MaxItemAmount - 1
			isMouseOn% = False
			If ScaledMouseX() > x And ScaledMouseX() < x + width Then
				If ScaledMouseY() > y And ScaledMouseY() < y + height Then
					isMouseOn = True
				EndIf
			EndIf
			
			If Inventory[n] <> Null Then
				Color 200, 200, 200
				Select Inventory[n]\itemtemplate\tempname 
					Case "gasmask","supergasmask","gasmask3"
						If wbi\GasMask > 0 Then Rect(x - 3, y - 3, width + 6, height + 6)
					Case "hazmatsuit","hazmatsuit2","hazmatsuit3"
						If wbi\Hazmat > 0 Then Rect(x - 3, y - 3, width + 6, height + 6)
					Case "scramble","veryfinescramble"
						If wbi\SCRAMBLE > 0 Then Rect(x - 3, y - 3, width + 6, height + 6)
					Case "nvgoggles","supernv","finenvgoggles"
						If wbi\NightVision > 0 Then Rect(x - 3, y - 3, width + 6, height + 6)
					Case "scp268","super268"
						If I_268\Using > 0 Then Rect(x - 3, y - 3, width + 6, height + 6)
					Case "scp402"
					    If I_402\Using > 0 Then Rect(x - 3, y - 3, width + 6, height + 6)
					Case "scp427"
						If I_427\Using > 0 Then Rect(x - 3, y - 3, width + 6, height + 6)
					Case "scp714"
						If I_714\Using > 0 Then Rect(x - 3, y - 3, width + 6, height + 6)
					Case "scp1033ru","super1033ru"
						If I_1033RU\Using > 0 Then Rect(x - 3, y - 3, width + 6, height + 6)
					Case "helmet","finehelmet"
						If wbi\Helmet > 0 Then Rect(x - 3, y - 3, width + 6, height + 6)
					Case "ntf_helmet","fine_ntf_helmet"
						If mpl\HasNTFGasmask > 0 Then Rect(x - 3, y - 3, width + 6, height + 6)
				End Select
			EndIf
			
			If isMouseOn Then
				MouseSlot = n
				Color 255, 0, 0
				Rect(x - 1, y - 1, width + 2, height + 2)
			EndIf
			
			Color 255, 255, 255
			DrawFrame(x, y, width, height, (x Mod 64), (x Mod 64))
			
			If Inventory[n] <> Null Then
				If (SelectedItem <> Inventory[n] Lor isMouseOn) Then 
					DrawImage(Inventory[n]\invimg, x + width / 2 - 32, y + height / 2 - 32)
				EndIf
			EndIf
			
			If Inventory[n] <> Null And SelectedItem <> Inventory[n] Then
				If isMouseOn Then
					If SelectedItem = Null
						Local spacing2# = 0
						x2# = x
						y2# = y+height
						Local width2# = width/3
						Local height2# = height/3
						
						If Inventory[n]\itemtemplate\isGun% Then
							SetFont fo\Font[Font_Default]
							For i = 0 To MaxGunSlots-1
								Color 255,255,255
								DrawFrame(x2+(width2*i), y2, width2, height2, (x Mod 64), (x Mod 64))
								If g_I\Weapon_CurrSlot = (i + 1) Then
									Color 255,0,0
								Else
									Color 255,255,255
								EndIf
								Text (x2+(width2*i))+(width2*0.5), y2+(height2*0.5), (i + 1), 1, 1
								spacing2# = 10
							Next
						EndIf
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						Text(x + width / 2 + 1, y + height + spacing + spacing2 - 15 + 1, Inventory[n]\name, True)							
						Color 255, 255, 255	
						Text(x + width / 2, y + height + spacing + spacing2 - 15, Inventory[n]\name, True)	
					EndIf
				EndIf
			Else
				
			EndIf					
			
			x=x+width + spacing
			If n = 4 Then 
				y = y + height*2 
				x = opt\GraphicWidth / 2 - (width * MaxItemAmount /2 + spacing * (MaxItemAmount / 2 - 1)) / 2
			EndIf
		Next
		
		width2# = 70
		height2# = 70
		spacing2# = 35
		
		x2# = opt\GraphicWidth / 2
		y2# = opt\GraphicHeight / 2 + height2*2.6
		
		Color 255,255,255
		SetFont fo\Font[Font_Default]
		Text x2,y2,GetLocalString("Singleplayer","weapon_slots"),1,1
		
		x2 = opt\GraphicWidth / 2 - (width2 * MaxItemAmount /2 + spacing2 * (MaxItemAmount / 2 - 1)) / 2
		y2 = opt\GraphicHeight / 2 - height2 + height2*4
		
		x2=x2+width2 + spacing2
		
		Color 255,255,255
		For i = 0 To MaxGunSlots-1
			Local hasGunInSlot% = False
			If g_I\Weapon_InSlot[i] <> "" Then
				For g = Each Guns
					If g\name = g_I\Weapon_InSlot[i] Then
						If g_I\Weapon_CurrSlot = (i + 1) Then
							Color 200,200,200
							Rect x2+((width2+spacing2)*i) - 3, y2 - 3, width2 + 6, height2 + 6
						EndIf
						Color 255,255,255
						DrawFrame(x2+((width2+spacing2)*i), y2, width2, height2, (x2 Mod 64), (x2 Mod 64))
						DrawImage g\IMG, (x2+((width2+spacing2)*i)) + width2 / 2 - 32, y2 + height2 / 2 - 32
						hasGunInSlot = True
						Exit
					EndIf
				Next
			EndIf
			If (Not hasGunInSlot) Then
				DrawFrame(x2+((width2+spacing2)*i), y2, width2, height2, (x2 Mod 64), (x2 Mod 64))
			EndIf
		Next
		
		If SelectedItem <> Null Then
			If MouseDown1 Then
				If MouseSlot = 66 Then
					DrawImage(SelectedItem\invimg, ScaledMouseX() - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, ScaledMouseY() - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
				ElseIf SelectedItem <> Inventory[MouseSlot]
					DrawImage(SelectedItem\invimg, ScaledMouseX() - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, ScaledMouseY() - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
				EndIf
			Else
				If MouseSlot = 66 Then
					
				Else
					
				EndIf
			EndIf
		EndIf
	Else
		If SelectedItem <> Null Then
			Select SelectedItem\itemtemplate\tempname
				Case "scp860", "hand", "hand2", "hand3", "25ct", "coin", "fuse", "fuse_purple", "scopebat","vanecoin"
					;[Block]
					DrawImage(SelectedItem\itemtemplate\invimg, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
					;[End Block]
				Case "key1", "key2", "key3", "key4", "key5", "key6", "key_cave", "key_cave2", "scp005","misc"
					;[Block]
					DrawImage(SelectedItem\itemtemplate\invimg, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
					;[End Block]
				Case "firstaid", "finefirstaid", "firstaid2"
					;[Block]
					If psp\Health < 100 Then
						If (Not wbi\Hazmat) Then
							DrawImage(SelectedItem\itemtemplate\invimg, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
							
							width% = 300
							height% = 20
							x% = opt\GraphicWidth / 2 - width / 2
							y% = opt\GraphicHeight / 2 + 80
							Rect(x, y, width+4, height, False)
							Color 110, 110, 110		
					        Rect(x + 3, y + 3, Float(SelectedItem\state * (width - 6) / 100), 14)
						EndIf
					EndIf
					;[End Block]
				Case "scp1102ru"
					;[Block]
					DrawImage(SelectedItem\itemtemplate\invimg, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
					
					width% = 300
					height% = 20
					x% = opt\GraphicWidth / 2 - width / 2
					y% = opt\GraphicHeight / 2 + 80
					Rect(x, y, width+4, height, False)
					Color 110, 110, 110
					Rect(x + 3, y + 3, Float(SelectedItem\state * (width - 6) / 100), 14)
					;[End Block]
				Case "gasmask","supergasmask","gasmask3"
					;[Block]
					If (Not mpl\HasNTFGasmask Lor wbi\Helmet Lor hds\Wearing Lor wbi\Hazmat Lor wbi\NightVision Lor wbi\SCRAMBLE) Then
						DrawImage(SelectedItem\itemtemplate\invimg, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
						
						width% = 300
						height% = 20
						x% = opt\GraphicWidth / 2 - width / 2
						y% = opt\GraphicHeight / 2 + 80
						Rect(x, y, width+4, height, False)
						Color 110, 110, 110
						Rect(x + 3, y + 3, Float(SelectedItem\state * (width - 6) / 100), 14)
					Else
						SelectedItem = Null
					EndIf
					;[End Block]
				Case "hazmatsuit","hazmatsuit2","hazmatsuit3"
					;[Block]
					If (Not wbi\GasMask Lor wbi\Helmet Lor hds\Wearing Lor mpl\HasNTFGasmask Lor wbi\NightVision Lor wbi\SCRAMBLE) Then
						DrawImage(SelectedItem\itemtemplate\invimg, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
						
						width% = 300
						height% = 20
						x% = opt\GraphicWidth / 2 - width / 2
						y% = opt\GraphicHeight / 2 + 80
						Rect(x, y, width+4, height, False)
						Color 110, 110, 110
						Rect(x + 3, y + 3, Float(SelectedItem\state * (width - 6) / 100), 14)
					Else
						SelectedItem = Null
					EndIf
					;[End Block]
				Case "ntf_helmet","fine_ntf_helmet"
					;[Block]
					If (Not wbi\GasMask Lor wbi\Helmet Lor hds\Wearing Lor wbi\Hazmat Lor wbi\NightVision Lor wbi\SCRAMBLE) Then
						DrawImage(SelectedItem\itemtemplate\invimg, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
						
						width% = 300
						height% = 20
						x% = opt\GraphicWidth / 2 - width / 2
						y% = opt\GraphicHeight / 2 + 80
						Rect(x, y, width+4, height, False)
						Color 110, 110, 110
						Rect(x + 3, y + 3, Float(SelectedItem\state * (width - 6) / 100), 14)
					Else
						SelectedItem = Null
					EndIf
					;[End Block]
				Case "helmet","finehelmet"
					;[Block]
					If (Not wbi\GasMask Lor mpl\HasNTFGasmask Lor hds\Wearing Lor wbi\Hazmat Lor wbi\NightVision Lor wbi\SCRAMBLE) Then
						DrawImage(SelectedItem\itemtemplate\invimg, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
						
						width% = 300
						height% = 20
						x% = opt\GraphicWidth / 2 - width / 2
						y% = opt\GraphicHeight / 2 + 80
						Rect(x, y, width+4, height, False)
						Color 110, 110, 110
						Rect(x + 3, y + 3, Float(SelectedItem\state * (width - 6) / 100), 14)
					Else
						SelectedItem = Null
					EndIf
					;[End Block]
				Case "nvgoggles","supernv","finenvgoggles"
					;[Block]
					If (Not wbi\GasMask Lor wbi\Helmet Lor hds\Wearing Lor mpl\HasNTFGasmask Lor wbi\Hazmat Lor wbi\SCRAMBLE) Then
						DrawImage(SelectedItem\itemtemplate\invimg, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
						
						width% = 300
						height% = 20
						x% = opt\GraphicWidth / 2 - width / 2
						y% = opt\GraphicHeight / 2 + 80
						Rect(x, y, width+4, height, False)
						Color 110, 110, 110
						Rect(x + 3, y + 3, Float(SelectedItem\state2 * (width - 6) / 100), 14)
					Else
						SelectedItem = Null
					EndIf
					;[End Block]
				Case "scramble","veryfinescramble"
					;[Block]
					If (Not wbi\GasMask Lor wbi\Helmet Lor hds\Wearing Lor mpl\HasNTFGasmask Lor wbi\Hazmat Lor wbi\NightVision) Then
						DrawImage(SelectedItem\itemtemplate\invimg, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
						
						width% = 300
						height% = 20
						x% = opt\GraphicWidth / 2 - width / 2
						y% = opt\GraphicHeight / 2 + 80
						Rect(x, y, width+4, height, False)
						Color 110, 110, 110
						Rect(x + 3, y + 3, Float(SelectedItem\state2 * (width - 6) / 100), 14)
					Else
						SelectedItem = Null
					EndIf
					;[End Block]
				Case "paper", "ticket"
					;[Block]
					If SelectedItem\itemtemplate\img = 0 Then
						Select SelectedItem\itemtemplate\name
							Case "Burnt Note" 
								SelectedItem\itemtemplate\img = LoadImage_Strict("GFX\items\burnt_note.it")
								SetBuffer ImageBuffer(SelectedItem\itemtemplate\img)
								Color 0,0,0
								SetFont fo\Font[Font_Default_Medium]
								Text 277, 469, AccessCode[0], True, True
								Color 255,255,255
								SetBuffer BackBuffer()
							Case "O5 Council Room Note","Document SCP-372", "Surveillance Room Password Note"
								SelectedItem\itemtemplate\img = LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
								SelectedItem\itemtemplate\img = ResizeImage2(SelectedItem\itemtemplate\img, ImageWidth(SelectedItem\itemtemplate\img) * MenuScale, ImageHeight(SelectedItem\itemtemplate\img) * MenuScale)
								
								SetBuffer ImageBuffer(SelectedItem\itemtemplate\img)
								
								Select SelectedItem\itemtemplate\name
									Case "O5 Council Room Note"
										Color 37,45,137
										SetFont fo\Font[Font_Journal]
										Text 240*MenuScale, 75*MenuScale, AccessCode[1], True, True
									Case "Document SCP-372"
										Color 37,45,137
										SetFont fo\Font[Font_Journal]
										Text 383*MenuScale, 734*MenuScale, AccessCode[2], True, True
									Case "Surveillance Room Password Note"
										Color 0,10,10
										SetFont fo\Font[Font_Journal]
										Text 220*MenuScale, 80*MenuScale, AccessCode[3], True, True
								End Select
								
								Color 255,255,255
								SetBuffer BackBuffer()
							Case "Movie Ticket"
								SelectedItem\itemtemplate\img=LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
							Case "Wolfnaya's Room Note"
								SelectedItem\itemtemplate\img=LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
								SelectedItem\itemtemplate\img = ResizeImage2(SelectedItem\itemtemplate\img, ImageWidth(SelectedItem\itemtemplate\img) * MenuScale, ImageHeight(SelectedItem\itemtemplate\img) * MenuScale)
								ecst\UnlockedWolfnaya = True
							Default 
								SelectedItem\itemtemplate\img=LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
								SelectedItem\itemtemplate\img = ResizeImage2(SelectedItem\itemtemplate\img, ImageWidth(SelectedItem\itemtemplate\img) * MenuScale, ImageHeight(SelectedItem\itemtemplate\img) * MenuScale)
						End Select
						
						MaskImage(SelectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					DrawImage(SelectedItem\itemtemplate\img, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\img) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\img) / 2)
					;[End Block]
				Case "scp1025"
					;[Block]
					If SelectedItem\itemtemplate\img=0 Then
						SelectedItem\itemtemplate\img=LoadImage_Strict("GFX\items\1025\1025_"+Int(SelectedItem\state)+".jpg")	
						SelectedItem\itemtemplate\img=ResizeImage2(SelectedItem\itemtemplate\img, ImageWidth(SelectedItem\itemtemplate\img) * MenuScale, ImageHeight(SelectedItem\itemtemplate\img) * MenuScale)
						
						MaskImage(SelectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					DrawImage(SelectedItem\itemtemplate\img, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\img) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\img) / 2)
					
					;[End Block]
				Case "radio","18vradio","fineradio","veryfineradio"
					;[Block]
					If SelectedItem\itemtemplate\img=0 Then
						SelectedItem\itemtemplate\img=LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
						MaskImage(SelectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					If RadioState[5] = 0 And SelectedItem\state > 0 Then
						CreateMsg(GetLocalString("Items","radio_1"))
					EndIf
					
					strtemp$ = ""
					
					x = opt\GraphicWidth - ImageWidth(SelectedItem\itemtemplate\img)
					y = opt\GraphicHeight - ImageHeight(SelectedItem\itemtemplate\img)
					
					DrawImage(SelectedItem\itemtemplate\img, x, y)
					
					If SelectedItem\state > 0 Then
						If PlayerRoom\RoomTemplate\Name <> "pocketdimension" Lor CoffinDistance > 4.0 Then
							Select Int(SelectedItem\state2)
								Case 0
									strtemp = GetLocalString("Devices","radio_track_found")
									If (Not EnableUserTracks)
										strtemp = strtemp + GetLocalString("Devices","radio_track_disabled")
									ElseIf UserTrackMusicAmount<1
										strtemp = strtemp + GetLocalString("Devices","radio_no_track")
									EndIf
								Case 1
									DebugLog RadioState[1]
									strtemp = GetLocalString("Devices","radio_cb_1")
								Case 2
									strtemp = GetLocalString("Devices","radio_scp")
								Case 3
									strtemp = GetLocalString("Devices","radio_cb_2")
							End Select 
							
							x=x+66
							y=y+419
							
							Color (30,30,30)
							
							If SelectedItem\state <= 100 Then
								For i = 0 To 4
									Rect(x, y+8*i, 43 - i * 6, 4, Ceil(SelectedItem\state / 20.0) > 4 - i )
								Next
							EndIf	
							
							SetFont fo\Font[Font_Digital_Small]
							Text(x+60, y, GetLocalString("Devices","radio_channel"))						
							
							If SelectedItem\itemtemplate\tempname = "veryfineradio" Then
								strtemp = ""
								For i = 0 To Rand(5, 30)
									strtemp = strtemp + Chr(Rand(1,100))
								Next
								
								SetFont fo\Font[Font_Digital_Large]
								Text(x+97, y+16, Rand(0,9),True,True)
							Else
								SetFont fo\Font[Font_Digital_Large]
								Text(x+97, y+16, Int(SelectedItem\state2+1),True,True)
							EndIf
							
							SetFont fo\Font[Font_Digital_Small]
							
							If strtemp <> "" Then
								strtemp = Right(Left(strtemp, (Int(MilliSecs()/300) Mod Len(strtemp))),10)
								Text(x+32, y+33, strtemp)
							EndIf
							
							SetFont fo\Font[Font_Default]
						EndIf
					EndIf
					
					;[End Block]
				Case "navigator", "nav"
					;[Block]
					If SelectedItem\itemtemplate\img=0 Then
						SelectedItem\itemtemplate\img=LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
						MaskImage(SelectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					x = opt\GraphicWidth - ImageWidth(SelectedItem\itemtemplate\img)*0.5+20
					y = opt\GraphicHeight - ImageHeight(SelectedItem\itemtemplate\img)*0.4-85
					width = 287
					height = 256
					
					Local PlayerX,PlayerZ
					
					DrawImage(SelectedItem\itemtemplate\img, x - ImageWidth(SelectedItem\itemtemplate\img) / 2, y - ImageHeight(SelectedItem\itemtemplate\img) / 2 + 85)
					
					SetFont fo\Font[Font_Digital_Small]
					
					If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then
						If (MilliSecs() Mod 1000) > 300 Then
							Color 30,30,30
							Text(x, y + height / 2 - 80, GetLocalString("Devices","nav_pd_1"), True)
							Text(x, y + height / 2 - 60, GetLocalString("Devices","nav_pd_2"), True)						
						EndIf
					Else
						
						If (SelectedItem\state > 0 Or SelectedItem\itemtemplate\name = "S-NAV Navigator Ultimate") And (Rnd(CoffinDistance + 15.0) > 1.0 Lor PlayerRoom\RoomTemplate\Name <> "cont_895") Then
							
							PlayerX% = Floor(EntityX(Collider) / 8.0 + 0.5)
							PlayerZ% = Floor(EntityZ(Collider) / 8.0 + 0.5)
							
							SetBuffer ImageBuffer(NavBG)
							Local xx = x-ImageWidth(SelectedItem\itemtemplate\img)/2
							Local yy = y-ImageHeight(SelectedItem\itemtemplate\img)/2+85
							DrawImage(SelectedItem\itemtemplate\img, xx, yy)
							
							Local posX# = EntityX(Collider) - 4.0
							Local posZ# = EntityZ(Collider) - 4.0
							Local stepsX% = 0
							Local stepsZ% = 0
							Local tempPos# = posX
							While tempPos < 0.0
								stepsX = stepsX + 1
								tempPos = tempPos + 8.0
							Wend
							tempPos# = posZ
							While tempPos < 0.0
								stepsZ = stepsZ + 1
								tempPos = tempPos + 8.0
							Wend
							x = x - 12 + ((posX + (8.0 * stepsX)) Mod 8.0) * 3
							y = y + 12 - ((posZ + (8.0 * stepsZ)) Mod 8.0) * 3
							For z2 = Max(0, PlayerZ - 6) To Min(MapGridSize - 1, PlayerZ + 6)
								For x2 = Max(0, PlayerX - 6) To Min(MapGridSize - 1, PlayerX + 6)
									If SelectedItem\itemtemplate\name = "S-NAV 300 Navigator" And Rand(0,1) Then Exit
									If CoffinDistance > 16.0 Lor Rnd(16.0) < CoffinDistance Then
										If CurrGrid\Grid[x2 + (z2 * MapGridSize)] Then
											Local drawx% = x + (PlayerX - x2) * 24 , drawy% = y - (PlayerZ - z2) * 24
											
											Color (30,30,30)
											If SelectedItem\itemtemplate\name = "S-NAV Navigator" Then Color(100, 0, 0)
											
											If MapTemp[(x2 + 1) * MapWidth + z2] = False Then Rect(drawx - (12 * MenuScale), drawy - (12 * MenuScale), MenuScale, 24 * MenuScale)
											If MapTemp[(x2 - 1) * MapWidth + z2] = False Then Rect(drawx + (12 * MenuScale), drawy - (12 * MenuScale), MenuScale, 24 * MenuScale)
											
											If MapTemp[x2 * MapWidth + z2 - 1] = False Then Rect(drawx - (12 * MenuScale), drawy - (12 * MenuScale), 24 * MenuScale, MenuScale)
											If MapTemp[x2 * MapWidth + z2 + 1] = False Then Rect(drawx - (12 * MenuScale), drawy + (12 * MenuScale), 24 * MenuScale, MenuScale)
										EndIf
									EndIf
								Next
							Next
							SetBuffer BackBuffer()
							DrawImageRect NavBG,xx+80,yy+70,xx+80,yy+70,270,230
							Color 30,30,30
							If SelectedItem\itemtemplate\name = "S-NAV Navigator" Then Color(100, 0, 0)
							Rect xx+80,yy+70,270,230,False
							
							x = opt\GraphicWidth - ImageWidth(SelectedItem\itemtemplate\img)*0.5+20
							y = opt\GraphicHeight - ImageHeight(SelectedItem\itemtemplate\img)*0.4-85
							
							If (MilliSecs() Mod 1000) > 300 Then
								If SelectedItem\itemtemplate\name <> "S-NAV 310 Navigator" And SelectedItem\itemtemplate\name <> "S-NAV Navigator Ultimate" Then
									Color(100, 0, 0)
									Text(x - width/2 + 10, y - height/2 + 10, GetLocalString("Devices","nav_low_signal"))
								EndIf
								
								Color(0, 150, 50)
								Oval(x-7,y-5,width/20,height/20) ; ~ Player
							EndIf
							
							If SelectedItem\itemtemplate\name = "S-NAV Navigator" Then 
								Color(100, 0, 0)
							Else
								Color (30,30,30)
							EndIf
							
							Local SCPs_found% = 0
							If SelectedItem\itemtemplate\name = "S-NAV Navigator Ultimate" And (MilliSecs() Mod 600) < 400 Then
								Local dist# = EntityDistanceSquared(Camera, Curr173\obj)
								If dist < PowTwo(8.0 * 4) Then
									dist = Sqr(dist)
									Color 100, 0, 0
									Oval(x - dist * 3, y - 7 - dist * 3, dist * 3 * 2, dist * 3 * 2, False)
									Text(x - width / 2 + 10, y - height / 2 + 30, "SCP-173")
									SCPs_found% = SCPs_found% + 1
								EndIf
								dist# = EntityDistanceSquared(Camera, Curr106\obj)
								If dist < PowTwo(8.0 * 4) Then
									dist = Sqr(dist)
									Color 100, 0, 0
									Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
									Text(x - width / 2 + 10, y - height / 2 + 30 + (20*SCPs_found), "SCP-106")
									SCPs_found% = SCPs_found% + 1
								EndIf
								If Curr096<>Null Then 
									dist# = EntityDistanceSquared(Camera, Curr096\obj)
									If dist < PowTwo(8.0 * 4) Then
										dist = Sqr(dist)
										Color 100, 0, 0
										Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
										Text(x - width / 2 + 10, y - height / 2 + 30 + (20*SCPs_found), "SCP-096")
										SCPs_found% = SCPs_found% + 1
									EndIf
								EndIf
								Local np.NPCs
								For np.NPCs = Each NPCs
									If np\NPCtype = NPCtype049
										dist# = EntityDistanceSquared(Camera, np\obj)
										If dist < PowTwo(8.0 * 4) Then
											dist = Sqr(dist)
											If (Not np\HideFromNVG)
												Color 100, 0, 0
												Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
												Text(x - width / 2 + 10, y - height / 2 + 30 + (20*SCPs_found), "SCP-049")
												SCPs_found% = SCPs_found% + 1
											EndIf
										EndIf
									EndIf
								Next
								If PlayerRoom\RoomTemplate\Name = "cont_895" Then
									If CoffinDistance < 8.0 Then
										dist = Rnd(4.0, 8.0)
										Color 100, 0, 0
										Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
										Text(x - width / 2 + 10, y - height / 2 + 30 + (20*SCPs_found), "SCP-895")
									EndIf
								EndIf
							End If
							
							Color (30,30,30)
							If SelectedItem\itemtemplate\name = "S-NAV Navigator" Then Color(100, 0, 0)
							If SelectedItem\state <= 100 Then
								xtemp = x - width/2 + 196
								ytemp = y - height/2 + 10
								Rect xtemp,ytemp,80,20,False
								; ~ Battery
								If SelectedItem\state <= 20.0 Then
									Color(100, 0, 0)
									If SelectedItem\itemtemplate\name = "S-NAV 310 Navigator" Then Text (x - width/2 + 10, y - height/2 + 10, GetLocalString("Devices","nav_bat_2"))
								Else
									Color(30, 30, 30)
									If SelectedItem\itemtemplate\name = "S-NAV 310 Navigator" Then 	Text (x - width/2 + 10, y - height/2 + 10, GetLocalString("Devices","nav_bat_1"))
								EndIf
								For i = 1 To Min(Ceil(SelectedItem\state / 10.0), 10.0)
									Rect(xtemp + ((i * 8) * MenuScale) - (6 * MenuScale), ytemp + (4 * MenuScale), 4 * MenuScale, 12 * MenuScale)
								Next
								SetFont(fo\Font[Font_Digital_Small])
							EndIf
						EndIf
					EndIf
					;[End Block]
				Case "badge"
					;[Block]
					If SelectedItem\itemtemplate\img=0 Then
						SelectedItem\itemtemplate\img=LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
						MaskImage(SelectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					DrawImage(SelectedItem\itemtemplate\img, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\img) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\img) / 2)
					;[End Block]
				Case "oldpaper"
					;[Block]
					If SelectedItem\itemtemplate\img = 0 Then
						SelectedItem\itemtemplate\img = LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
						SelectedItem\itemtemplate\img = ResizeImage2(SelectedItem\itemtemplate\img, ImageWidth(SelectedItem\itemtemplate\img) * MenuScale, ImageHeight(SelectedItem\itemtemplate\img) * MenuScale)
						
						MaskImage(SelectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					DrawImage(SelectedItem\itemtemplate\img, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\img) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\img) / 2)
					;[End Block]
			End Select
			
		EndIf		
	EndIf
	
	If hds\Wearing Then
		If hds\ExplodeTimer > 0 Then
			If (MilliSecs() Mod 1000) > 300 Then
				SetFont fo\Font[Font_Digital_Large]
				Color 255,0,0
				Text(opt\GraphicWidth/2, opt\GraphicHeight/2, GetLocalString("Hazardous Suit","warning"),True,True)
				SetFont fo\Font[Font_Digital_Medium]
				Text(opt\GraphicWidth/2, opt\GraphicHeight/2+50, GetLocalString("Hazardous Suit","systems_critical"),True,True)
			EndIf
		EndIf
	EndIf
	
	DrawTasks()
	
	RenderCommunicationAndSocialWheel()
	
	CatchErrors("DrawGUI")
End Function

Function DrawGunsInHUD()
	Local isMultiplayer% = (gc\CurrGamemode = 3)
	Local g.Guns
	Local x# = 50, x2# = 150
	Local y# = 50
	Local width#=64,height#=64
	
	Local width2%
	Local i%
	width# = 204
	height# = 20
	
	x# = opt\GraphicWidth - 60
	y# = opt\GraphicHeight - 55
	
	Local pAmmo%, pReloadAmmo%, pAmmo2%, pReloadAmmo2%
	If isMultiplayer Then
		pAmmo = Players[mp_I\PlayerID]\Ammo[Players[mp_I\PlayerID]\SelectedSlot]
		pReloadAmmo = Players[mp_I\PlayerID]\ReloadAmmo[Players[mp_I\PlayerID]\SelectedSlot]
	Else
		For g = Each Guns
			If g\ID = g_I\HoldingGun Then
				pAmmo = g\CurrAmmo
				pReloadAmmo = g\CurrReloadAmmo
				pAmmo2 = g\CurrAltAmmo
				pReloadAmmo2 = g\CurrReloadAltAmmo
			EndIf
		Next
	EndIf
	
	For g = Each Guns
		If g\ID = g_I\HoldingGun Then
			; ~ For Aiming Cordinates
			If IsUsingAimCross Then
				DrawImage AimCrossIMG,opt\GraphicWidth/2,opt\GraphicHeight/2
			EndIf
			; ~ For Aiming Cordinates
			If (g\GunType <> GUNTYPE_MELEE And g\GunType <> GUNTYPE_GRENADE) Then
				
				If pAmmo > 0 Then
					Color 255,255,255
				Else
					Color 255,0,0
				EndIf
				Rect(x - 50 - 1 - 30, y - 1, 30 + 2, 30 + 2, False)
				
				If g\GunType = GUNTYPE_RIFLE Lor g\GunType = GUNTYPE_OICW Then
					DrawImage RifleBulletIcon, x - 50 - 30, y
				ElseIf g\GunType = GUNTYPE_SMG Lor g\GunType = GUNTYPE_HANDGUN Lor g\GunType = GUNTYPE_HANDGUN_AUTO Then
					DrawImage BulletIcon, x - 50 - 30, y
				ElseIf g\GunType = GUNTYPE_SHOTGUN Then
					DrawImage ShotgunBulletIcon, x - 50 - 30, y
				ElseIf g\GunType = GUNTYPE_SCP_127 Then
					DrawImage ToothIcon, x - 50 - 30, y
				ElseIf g\GunType = GUNTYPE_EMRP Then
					DrawImage BoltIcon, x - 50 - 30, y
				EndIf
				
				If g\GunType = GUNTYPE_SMG Lor g\GunType = GUNTYPE_RIFLE Lor g\GunType = GUNTYPE_HANDGUN_AUTO Then
					If (Not g\ChangeFiremode) Then
						SetFont fo\Font%[Font_Default_Medium]
						Color 255,255,255
						TextWithAlign x+20, y - 55, GetLocalString("Weapons","auto"),2
						
						Color 25,25,25
						TextWithAlign x+20, y - 25, GetLocalString("Weapons","semi_auto"),2
					Else
						SetFont fo\Font%[Font_Default_Medium]
						Color 25,25,25
						TextWithAlign x+20, y - 55, GetLocalString("Weapons","auto"),2
						
						Color 255,255,255
						TextWithAlign x+20, y - 25, GetLocalString("Weapons","semi_auto"),2
					EndIf
				ElseIf g\GunType = GUNTYPE_HANDGUN Then
					SetFont fo\Font%[Font_Default_Medium]
					Color 255,255,255
					TextWithAlign x+20, y - 25, GetLocalString("Weapons","semi_auto"),2
				ElseIf g\GunType = GUNTYPE_SHOTGUN Then
					SetFont fo\Font%[Font_Default_Medium]
					Color 255,255,255
					TextWithAlign x+20, y - 25, GetLocalString("Weapons","pump"),2
				ElseIf g\GunType = GUNTYPE_OICW
					
					If (Not g\ChangeFiremode) Then
						SetFont fo\Font%[Font_Default_Medium]
						Color 255,255,255
						TextWithAlign x+20, y - 105, GetLocalString("Weapons","auto"),2
						
						Color 25,25,25
						TextWithAlign x+20, y - 75, GetLocalString("Weapons","grenade"),2
					Else
						SetFont fo\Font%[Font_Default_Medium]
						Color 25,25,25
						TextWithAlign x+20, y - 105, GetLocalString("Weapons","auto"),2
						
						Color 255,255,255
						TextWithAlign x+20, y - 75, GetLocalString("Weapons","grenade"),2
					EndIf
					
					If pAmmo2 > 0 Then
						Color 255,255,255
					Else
						Color 255,0,0
					EndIf
					Rect(x - 50 - 1 - 30, y - 46, 30 + 2, 30 + 2, False)
					DrawImage GrenadeIcon, x - 50 - 30, y-45
					
					If pAmmo2 > g\MaxCurrAltAmmo/5
						Color 0,255,0
					Else
						Color 255,0,0
					EndIf
					SetFont fo\Font%[Font_Default_Medium]
					TextWithAlign x, y -40, pAmmo2, 2
					Color 0,255,0
					Text x, y -40, "/"
					width2% = StringWidth("/")
					If pReloadAmmo2 > 0
						Color 0,255,0
					Else
						Color 255,0,0
					EndIf
					Text x + width2, y -40, pReloadAmmo2
				ElseIf g\GunType = GUNTYPE_EMRP Then
					
					SetFont fo\Font%[Font_Default_Medium]
					Color 255,255,255
					TextWithAlign x+20, y - 25, GetLocalString("Weapons","magnets"),2
					
					If g\HasMUI Then
						
						If g\ChangeFiremode Then
							
							SetFont fo\Font%[Font_Default_Medium]
							Color 255,255,255
							TextWithAlign x+20, y - 110, GetLocalString("Weapons","open_mui"),2
							
							Color 25,25,25
							TextWithAlign x+20, y - 80, GetLocalString("Weapons","close_mui"),2
							
							Color 255,255,255
							Rect (x - 150, y - 60, width, height, False)
							
							If g\ChargeTimer2 < 0.63*70 Then
								Color 255, 25, 25
							Else
								Color 25, 255, 25
							EndIf
							Rect(x - 150 + 3, y - 60 + 3, Min(Float(g\ChargeTimer2 * (width - 6) / (70*1.26)),width - 6), 14)
							If pAmmo = 0 Then
								DrawImage XIcon, x - 50, y - 60
							EndIf
						Else
							SetFont fo\Font%[Font_Default_Medium]
							Color 25,25,25
							TextWithAlign x+20, y - 110, GetLocalString("Weapons","open_mui"),2
							
							Color 255,255,255
							TextWithAlign x+20, y - 80, GetLocalString("Weapons","close_mui"),2
						EndIf
					EndIf
					
				EndIf
				
				SetFont fo\Font%[Font_Digital_Medium]
				If pAmmo > g\MaxCurrAmmo/5
					Color 0,255,0
				Else
					Color 255,0,0
				EndIf
				TextWithAlign x, y + 5, pAmmo, 2
				Color 0,255,0
				If (Not g\GunType = GUNTYPE_SCP_127) Then
					Text x, y + 5, "/"
					width2% = StringWidth("/")
					If pReloadAmmo > 0
						Color 0,255,0
					Else
						Color 255,0,0
					EndIf
					Text x + width2, y + 5, pReloadAmmo
				EndIf
			EndIf
			Exit
		EndIf
	Next
	
	Color 255,255,255
	
	x = 55
	y = 55
	width = 64
	height = 64
	If mpl\SlotsDisplayTimer > 0.0 Then
		For i = 0 To MaxGunSlots-1
			DrawFrame((x-3)+(128*i),y-3,width+6,height+6)
			If g_I\Weapon_CurrSlot = (i + 1) Then
				Color 0,255,0
				Rect (x-3)+(128*i),y-3,width+6,height+6,True
			EndIf
			If g_I\Weapon_InSlot[i] <> "" Then
				For g = Each Guns
					If g\name = g_I\Weapon_InSlot[i] Then
						DrawImage g\IMG,x+(128*i),y
						Color 255,255,255
						If g_I\Weapon_CurrSlot = (i + 1) Then
							SetFont fo\Font[Font_Default]
							Text(x+(width/2)+(128*i),y+height+10,g\DisplayName,True,False)
						EndIf
						Exit
					EndIf
				Next
			EndIf
		Next
	EndIf
	
	Color 255,255,255
	
End Function

Function DrawDebugHUD()
	Local npc.NPCs, x%, y%
	Local offset%
	Local ev.Events, it.Items,g.Guns
	
	If DebugHUD Then
		x% = 80
		
		Color 255, 255, 255
		SetFont fo\ConsoleFont
		
		Text x + 50, 20, "Delta time: "+ft\DeltaTime
		Text x - 50, 50, "Player Position: (" + f2s(EntityX(Collider), 3) + ", " + f2s(EntityY(Collider), 3) + ", " + f2s(EntityZ(Collider), 3) + ")"
		Text x - 50, 70, "Camera Position: (" + f2s(EntityX(Camera), 3)+ ", " + f2s(EntityY(Camera), 3) +", " + f2s(EntityZ(Camera), 3) + ")"
		Text x - 50, 100, "Player Rotation: (" + f2s(EntityPitch(Collider), 3) + ", " + f2s(EntityYaw(Collider), 3) + ", " + f2s(EntityRoll(Collider), 3) + ")"
		Text x - 50, 120, "Camera Rotation: (" + f2s(EntityPitch(Camera), 3)+ ", " + f2s(EntityYaw(Camera), 3) +", " + f2s(EntityRoll(Camera), 3) + ")"
		Text x - 50, 150, "Map seed: "+RandomSeed
		Text x - 50, 170, "Room: " + PlayerRoom\RoomTemplate\Name
		For ev.Events = Each Events
			If ev\room = PlayerRoom Then
				Text x - 50, 190, "Room event: " + ev\EventName   
				Text x - 50, 205, "state: " + ev\EventState[0]
				Text x - 50, 220, "state2: " + ev\EventState[1]
				Text x - 50, 235, "state3: " + ev\EventState[2]
				Text x - 50, 250, "state4: " + ev\EventState[3]
				Text x - 50, 265, "state5: " + ev\EventState[4]
;				Text x - 50, 280, "state5: " + ev\EventState[5]
;				Text x - 50, 295, "state5: " + ev\EventState[6]
;				Text x - 50, 310, "state5: " + ev\EventState[7]
;				Text x - 50, 325, "state5: " + ev\EventState[8]
;				Text x - 50, 340, "str: " + ev\EventStr
				Exit
			EndIf
		Next
		Text x - 50, 285, "Room coordinates: (" + Floor(EntityX(PlayerRoom\obj) / 8.0 + 0.5) + ", " + Floor(EntityZ(PlayerRoom\obj) / 8.0 + 0.5) + ", angle: "+PlayerRoom\angle + ")"
		Text x - 50, 310, "Current Trigger: " + CheckTriggers()
		Text x - 50, 350, "Stamina: " + f2s(Stamina, 3)
		Text x - 50, 360, "Death timer: " + f2s(KillTimer, 3)               
		Text x - 50, 380, "Blink timer: " + f2s(BlinkTimer, 3)
		Text x - 50, 400, "Health: " + psp\Health
		Text x - 50, 420, "Kevlar: " + psp\Kevlar
		;Text x - 50, 440, "Karma: " + psp\Karma
		If Curr173 <> Null
			Text x - 50, 460, "SCP - 173 Position (Collider): (" + f2s(EntityX(Curr173\Collider), 3) + ", " + f2s(EntityY(Curr173\Collider), 3) + ", " + f2s(EntityZ(Curr173\Collider), 3) + ")"
			Text x - 50, 480, "SCP - 173 Position (obj): (" + f2s(EntityX(Curr173\obj), 3) + ", " + f2s(EntityY(Curr173\obj), 3) + ", " + f2s(EntityZ(Curr173\obj), 3) + ")"
			Text x - 50, 500, "SCP - 173 State: " + Curr173\State
		EndIf
		If Curr106 <> Null
			Text x - 50, 520, "SCP - 106 Position: (" + f2s(EntityX(Curr106\obj), 3) + ", " + f2s(EntityY(Curr106\obj), 3) + ", " + f2s(EntityZ(Curr106\obj), 3) + ")"
			Text x - 50, 540, "SCP - 106 Idle: " + Curr106\Idle
			Text x - 50, 560, "SCP - 106 State: " + Curr106\State
		EndIf
		offset% = 0
		For npc.NPCs = Each NPCs
			If npc\NPCtype = NPCtype096 Then
				Text x - 50, 580, "SCP - 096 Position: (" + f2s(EntityX(npc\obj), 3) + ", " + f2s(EntityY(npc\obj), 3) + ", " + f2s(EntityZ(npc\obj), 3) + ")"
				Text x - 50, 600, "SCP - 096 Idle: " + npc\Idle
				Text x - 50, 620, "SCP - 096 State: " + npc\State
				Text x - 50, 640, "SCP - 096 Speed: " + f2s(npc\CurrSpeed, 5)
			EndIf
		Next
		Text x + 350, 50, "Current Room Position: ("+PlayerRoom\x+", "+PlayerRoom\y+", "+PlayerRoom\z+")"
		
		Text x + 350, 90, SystemProperty("os")+" "+gv\OSBit+" bit, CPU: "+SystemProperty("cpuname")+" (Arch: "+SystemProperty("cpuarch")+", "+GetEnv("NUMBER_OF_PROCESSORS")+" Threads)"
		Text x + 350, 110, "Phys. Memory: "+(AvailPhys()/1024)+" MB/"+(TotalPhys()/1024)+" MB ("+(AvailPhys())+" KB/"+(TotalPhys())+" KB). CPU Usage: "+MemoryLoad()+"%"
		Text x + 350, 130, "Virtual Memory: "+(AvailVirtual()/1024)+" MB/"+(TotalVirtual()/1024)+" MB ("+(AvailVirtual())+" KB/"+(TotalVirtual())+" KB)"
		Text x + 350, 150, "Video Memory: "+(AvailVidMem()/1024)+" MB/"+(TotalVidMem()/1024)+" MB ("+(AvailVidMem())+" KB/"+(TotalVidMem())+" KB)"
		Text x + 350, 170, "Triangles rendered: "+CurrTrisAmount
		Text x + 350, 190, "Active textures: "+ActiveTextures()
		Text x + 350, 230, "scp207 drinks: " + I_207\Factor
		Text x + 350, 250, "deathtimer of scp207: " + I_207\DeathTimer
		Text x + 350, 290, "HDS BootUpTimer: " + hds\BootUpTimer
		Text x + 350, 330, "Was In 076?: " + ecst\WasIn076
		Text x + 350, 350, "after 076 timer: " + ecst\After076Timer
		For g = Each Guns
			Text x + 350, 390, "EMR-P CHARGE: " + g\ChargeTimer2
		Next
		Text x + 350, 420, "OmegaAct " + ecst\OmegaWarheadDetonate
		Text x + 350, 440, "OmegaT " + ecst\OmegaWarheadTimer
		
		Text x + 350, 480, "InterAct " + ecst\IntercomEnabled
		Text x + 350, 500, "InterT " + ecst\IntercomTimer
		
		SetFont fo\Font[Font_Default]
	EndIf
	
End Function

Function DrawMenu()
	CatchErrors("Uncaught (DrawMenu)")
	
	Local x%, y%, width%, height%
	If Not InFocus() Then ;Game is out of focus -> pause the game
        MenuOpen = True
        PauseSounds()
        Delay 1000 ;Reduce the CPU take while game is not in focus
    EndIf
	If MenuOpen Then
		If KillTimer >= 0 Then
			CameraProjMode Camera, 0
			CameraProjMode m_I\Cam, 1
			PositionEntity m_I\Cam,0,-1000,0
			ShowEntity m_I\MenuLogo\logo
			ShowEntity m_I\MenuLogo\gradient
			RenderWorld
			CameraProjMode Camera, 1
			CameraProjMode m_I\Cam, 0
			HideEntity m_I\MenuLogo\logo
			HideEntity m_I\MenuLogo\gradient
			
			Color 255, 255, 255
			SetFont fo\Font[Font_Default]
			Text 20, opt\GraphicHeight-90, GetLocalString("Menu", "difficulty")+": "+SelectedDifficulty\Name
			Text 20, opt\GraphicHeight-70, GetLocalString("Menu", "save_name")+": "+CurrSave\Name
			Text 20, opt\GraphicHeight-50, GetLocalString("Menu", "map_seed")+": "+RandomSeed
			Text 20, opt\GraphicHeight-30, "v"+VersionNumber
			
			RenderMainMenu()
		Else
			;[Block]
			width = ImageWidth(PauseMenuIMG[0])
			height = ImageHeight(PauseMenuIMG[0])
			x = opt\GraphicWidth / 2 - width / 2
			y = opt\GraphicHeight / 2 - height / 2
			
			DrawImage PauseMenuIMG[0], x, y
			
			Color(255, 255, 255)
			
			x = x+132*MenuScale
			y = y+122*MenuScale
			
			
			SetFont fo\Font[Font_Menu]
			
			Text(x, y-(122-45)*MenuScale, Upper(GetLocalString("Menu", "you_died")),False,True)
			SetFont fo\Font[Font_Default]
			
			DrawAllMenuButtons()
			
			y = y+104*MenuScale
			If (Not GameSaved) Or SelectedDifficulty\PermaDeath Then
				Color 50,50,50
				Text(x + 185*MenuScale, y + 30*MenuScale, GetLocalString("Menu", "loadgame"), True, True)
			EndIf
			y= y + 80*MenuScale
			
			SetFont fo\Font[Font_Default]
			Color(255, 255, 255)
			RowText(m_msg\DeathTxt$, x, y + 80*MenuScale, 390*MenuScale, 600*MenuScale)
			;[End Block]
		EndIf
	EndIf
	
	SetFont fo\Font[Font_Default]
	
	Local AchvXIMG% = (x + (22*MenuScale))
	Local scale# = opt\GraphicHeight/768.0
	Local SeparationConst% = 76*scale
	Local imgsize% = 64
	Local i
	
	If AchievementsMenu>0 Then
		For i=0 To 11
			If i+((AchievementsMenu-1)*12)<MAXACHIEVEMENTS Then
				DrawAchvIMG(AchvXIMG,y+((i/4)*120*MenuScale),i+((AchievementsMenu-1)*12))
			Else
				Exit
			EndIf
		Next
		
		For i=0 To 11
			If i+((AchievementsMenu-1)*12)<MAXACHIEVEMENTS Then
				If MouseOn(AchvXIMG+((i Mod 4)*SeparationConst),y+((i/4)*120*MenuScale),64*scale,64*scale) Then
					AchievementTooltip(i+((AchievementsMenu-1)*12))
					Exit
				EndIf
			Else
				Exit
			EndIf
		Next
		
	EndIf
	
	CatchErrors("DrawMenu")
End Function

Function UpdateMenu()
	CatchErrors("Uncaught (UpdateMenu)")
	Local x%, y%, width%, height%
	Local r.Rooms
	
	If MenuOpen
		If KillTimer >= 0 Then
			ShowEntity m_I\Sprite
			UpdateMainMenu()
		Else
			;[Block]
			width = ImageWidth(PauseMenuIMG[0])
			height = ImageHeight(PauseMenuIMG[0])
			x = opt\GraphicWidth / 2 - width / 2
			y = opt\GraphicHeight / 2 - height / 2
			
			x = x+132*MenuScale
			y = y+122*MenuScale	
			
			y = y+104*MenuScale
			If GameSaved And (Not SelectedDifficulty\PermaDeath) Then
				If DrawButton(x, y, 390*MenuScale, 60*MenuScale, GetLocalString("Menu", "loadgame")) Then
					
					DeleteMenuGadgets()
					MenuOpen = False
					LoadGameQuick(SavePath + CurrSave\Name + "\")
					
					MoveMouse Viewport_Center_X,Viewport_Center_Y
					HidePointer()
					
					KillSounds()
					
					Playable=True
					
					UpdateRooms()
					UpdateDoors()
					
					For r.Rooms = Each Rooms
						x = Abs(EntityX(Collider) - EntityX(r\obj))
						Local z = Abs(EntityZ(Collider) - EntityZ(r\obj))
						
						If x < 12.0 And z < 12.0 Then
							MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)] = Max(MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)], 1)
							If x < 4.0 And z < 4.0 Then
								If Abs(EntityY(Collider) - EntityY(r\obj)) < 1.5 Then PlayerRoom = r
								MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)] = 1
							EndIf
						End If
					Next
					
					PlaySound_Strict LoadTempSound(("SFX\Horror\Horror8.ogg"))
					
					DropSpeed=0
					
					UpdateWorld 0.0
					
					fps\PrevTime = MilliSecs()
					fps\Factor[0] = 0
					
					ResetInput()
					
					ResumeSounds()
					Return
				EndIf
			Else
				DrawButton(x, y, 390*MenuScale, 60*MenuScale, "")
			EndIf
			If DrawButton(x, y + 80*MenuScale, 390*MenuScale, 60*MenuScale, GetLocalString("Menu", "quit_to_menu")) Then
				MainMenuOpen = True
				NullGame()
				MenuOpen = False
				MainMenuTab = 0
				CurrSave = Null
				FlushKeys()
				Return
			EndIf
			;[End Block]
		EndIf
	Else
		HideEntity m_I\Sprite
	EndIf
	
	If AchievementsMenu>0 Then
		If AchievementsMenu <= Floor(Float(MAXACHIEVEMENTS-1)/12.0) Then 
			If DrawButton(x+341*MenuScale, y + 344*MenuScale, 50*MenuScale, 60*MenuScale, ">") Then
				AchievementsMenu = AchievementsMenu+1
				ShouldDeleteGadgets=True
			EndIf
		EndIf
		If AchievementsMenu > 1 Then
			If DrawButton(x+41*MenuScale, y + 344*MenuScale, 50*MenuScale, 60*MenuScale, "<") Then
				AchievementsMenu = AchievementsMenu-1
				ShouldDeleteGadgets=True
			EndIf
		EndIf
	EndIf
	
	CatchErrors("UpdateMenu")
End Function

Function MouseOn%(x%, y%, width%, height%)
	If ScaledMouseX() > x And ScaledMouseX() < x + width Then
		If ScaledMouseY() > y And ScaledMouseY() < y + height Then
			Return True
		End If
	End If
	Return False
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D