Function Graphics3DExt%(width%,height%,depth%=32,mode%=2)
	
	Graphics3D width,height,depth,mode
	SetGfxDriver(CountGfxDrivers())
	TextureFilter "", 8192
	InitFastResize()
	
End Function

Function ResizeImage2(image%,width%,height%)
    Local img% = CreateImage(width,height)
	
	Local oldWidth% = ImageWidth(image)
	Local oldHeight% = ImageHeight(image)
	CopyRect 0,0,oldWidth,oldHeight,2048-oldWidth/2,2048-oldHeight/2,ImageBuffer(image),TextureBuffer(Fresize_Texture)
	SetBuffer BackBuffer()
	ScaleRender(0,0,4096.0 / Float(RealGraphicWidth) * Float(width) / Float(oldWidth), 4096.0 / Float(RealGraphicWidth) * Float(height) / Float(oldHeight))
	CopyRect RealGraphicWidth/2-width/2,RealGraphicHeight/2-height/2,width,height,0,0,BackBuffer(),ImageBuffer(img)
	
    FreeImage image
    Return img
End Function

Function ScaleImage2%(SrcImage%, ScaleX#, ScaleY#, ExactSize% = False)
	Local SrcWidth%, SrcHeight%
	Local DestWidth%, DestHeight%
	Local ScratchImage%, DestImage%
	Local SrcBuffer%, ScratchBuffer%, DestBuffer%
	Local X1%, Y1%, X2%, Y2%
	
	; ~ Get the width and height of the source image
	SrcWidth = ImageWidth(SrcImage)
	SrcHeight = ImageHeight(SrcImage)
	
	; ~ Calculate the width and height of the dest image, or the scale
	If (Not ExactSize) Then
		DestWidth = Floor(SrcWidth * ScaleX)
		DestHeight = Floor(SrcHeight * ScaleY)
	Else
		DestWidth = ScaleX
		DestHeight = ScaleY
		
		ScaleX = Float(DestWidth) / Float(SrcWidth)
		ScaleY = Float(DestHeight) / Float(SrcHeight)
	EndIf
	
	; ~ If the image does not need to be scaled, just copy the image and exit the function
	If (SrcWidth = DestWidth) And (SrcHeight = DestHeight) Then Return(CopyImage(SrcImage))
	
	; ~ Create a scratch image that is as tall as the source image, and as wide as the destination image
	ScratchImage = CreateImage(DestWidth, SrcHeight)
	
	; ~ Create the destination image
	DestImage = CreateImage(DestWidth, DestHeight)
	
	; ~ Get pointers to the image buffers
	SrcBuffer = ImageBuffer(SrcImage)
	ScratchBuffer = ImageBuffer(ScratchImage)
	DestBuffer = ImageBuffer(DestImage)
	
	; ~ Duplicate columns from source image to scratch image
	For X2 = 0 To DestWidth - 1
		X1 = Floor(X2 / ScaleX)
		CopyRect(X1, 0, 1, SrcHeight, X2, 0, SrcBuffer, ScratchBuffer)
	Next
	
	; ~ Duplicate rows from scratch image to destination image
	For Y2 = 0 To DestHeight - 1
		Y1 = Floor(Y2 / ScaleY)
		CopyRect(0, Y1, DestWidth, 1, 0, Y2, ScratchBuffer, DestBuffer)
	Next
	
	; ~ Free the scratch image
	FreeImage(ScratchImage)
	
	; ~ Return the new image
	Return(DestImage)
End Function

Function UpdateBatteryTimer%()
	
	BatMsgTimer = BatMsgTimer + fps\Factor[0]
	If BatMsgTimer >= 70.0 * 1.5 Then BatMsgTimer = 0.0
	
End Function

Function UpdateWorld2%()
	Local np.NPCs
	Local i%
	
	IsNVGBlinking = False
	
	Local HasBattery%
	Local Power%
	
	If (wbi\NightVision > 0 And wbi\NightVision <> 3) Lor (wbi\SCRAMBLE = 1) Then
		For i = 0 To MaxItemAmount - 1
			If Inventory[i] <> Null Then
				If (wbi\NightVision = 1 And Inventory[i]\itemtemplate\tempname = "nvgoggles") Lor (wbi\NightVision = 2 And Inventory[i]\itemtemplate\tempname = "supernv") Lor (wbi\SCRAMBLE = 1 And Inventory[i]\itemtemplate\tempname = "scramble") Then
					Inventory[i]\state = Max(0.0, Inventory[i]\state - (fps\Factor[0] * (0.02 * wbi\NightVision) + (0.15 * (wbi\SCRAMBLE))))
					Power = Int(Inventory[i]\state)
					If Power = 0 Then ; ~ This NVG or SCRAMBLE can't be used
						HasBattery = 0
						If wbi\SCRAMBLE > 0 Then
							CreateMsg(GetLocalString("Items","scramble_died"))
						Else
							CreateMsg(GetLocalString("Items","nvg_died"))
						EndIf
						IsNVGBlinking = True
						BlinkTimer = -1.0
					ElseIf Power <= 100
						HasBattery = 1
					Else
						HasBattery = 2
					EndIf
					Exit
				EndIf
			EndIf
		Next
		
		If wbi\NightVision = 2 Then
			If wbi\NightVisionTimer <= 0.0 Then
				For np.NPCs = Each NPCs
					np\NVX = EntityX(np\Collider, True)
					np\NVY = EntityY(np\Collider, True)
					np\NVZ = EntityZ(np\Collider, True)
				Next
				If wbi\NightVisionTimer <= -10.0 Then wbi\NightVisionTimer = 600.0
				IsNVGBlinking = True
			EndIf
			wbi\NightVisionTimer = wbi\NightVisionTimer - fps\Factor[0]
		EndIf
	EndIf
	
	If wbi\SCRAMBLE = 1 And Power = 0 Then
		CreateMsg(GetLocalString("Items","scramble_died"))
		wbi\SCRAMBLE = False
	EndIf
	
	If mpl\HasNTFGasmask = 2 And mpl\NightVisionEnabled Then
		If wbi\NightVisionTimer <= 0.0 Then
			For np.NPCs = Each NPCs
				np\NVX = EntityX(np\Collider, True)
				np\NVY = EntityY(np\Collider, True)
				np\NVZ = EntityZ(np\Collider, True)
			Next
			If wbi\NightVisionTimer <= -10.0 Then wbi\NightVisionTimer = 600.0
			IsNVGBlinking = True
		EndIf
		wbi\NightVisionTimer = wbi\NightVisionTimer - fps\Factor[0]
	EndIf
	
	If wbi\SCRAMBLE > 0 Then
		If wbi\SCRAMBLE = 1 Then
			If HasBattery <> 0 Then
				If (Not ChannelPlaying(SCRAMBLECHN)) Then
					SCRAMBLECHN = PlaySound_Strict(SCRAMBLESFX)
				EndIf
			Else
				StopChannel(SCRAMBLECHN)
			EndIf
		Else
			If (Not ChannelPlaying(SCRAMBLECHN)) Then
				SCRAMBLECHN = PlaySound_Strict(SCRAMBLESFX)
			EndIf
		EndIf
	Else
		StopChannel(SCRAMBLECHN)
	EndIf
	
	If fps\Factor[0] > 0.0 Then
		If HasBattery = 1 And ((MilliSecs() Mod 800) < 200) Then
			If (Not LowBatteryCHN[1]) Then
				LowBatteryCHN[1] = PlaySound_Strict(LowBatterySFX[1])
			ElseIf (Not ChannelPlaying(LowBatteryCHN[1])) Then
				LowBatteryCHN[1] = PlaySound_Strict(LowBatterySFX[1])
			EndIf
		EndIf
	EndIf
End Function

Const NVBrightness# = 30.0

Function RenderWorld2%(Tween#)
	Local np.NPCs
	Local i%, k%, l%
	
	CameraProjMode(Ark_Blur_Cam, 0)
	CameraProjMode(Camera, 1)
	
	If mpl\NightVisionEnabled Then
		AmbientLight(Min(NVBrightness * 2.0, 255.0), Min(NVBrightness * 2.0, 255.0), Min(NVBrightness * 2.0, 255.0))
	EndIf
	
	If wbi\NightVision > 0 And wbi\NightVision < 3 Then
		AmbientLight(Min(NVBrightness * 2.0, 255.0), Min(NVBrightness * 2.0, 255.0), Min(NVBrightness * 2.0, 255.0))
	ElseIf wbi\NightVision = 3
		AmbientLight(255.0, 255.0, 255.0)
	ElseIf PlayerRoom <> Null
		AmbientLight(NVBrightness, NVBrightness, NVBrightness)
	EndIf
	
	CameraViewport(Camera, 0, 0, opt\GraphicWidth, opt\GraphicHeight)
	
	Local HasBattery%
	Local Power%
	
	If (wbi\NightVision > 0 And wbi\NightVision <> 3) Lor (wbi\SCRAMBLE = 1) Then
		For i = 0 To MaxItemAmount - 1
			If Inventory[i] <> Null Then
				If (wbi\NightVision = 1 And Inventory[i]\itemtemplate\tempname = "nvgoggles") Lor (wbi\NightVision = 2 And Inventory[i]\itemtemplate\tempname = "supernv") Lor (wbi\SCRAMBLE = 1 And Inventory[i]\itemtemplate\tempname = "scramble") Then
					Power = Int(Inventory[i]\state)
					If Power = 0 Then ; ~ This NVG or SCRAMBLE can't be used
						HasBattery = 0
					ElseIf Power <= 100
						HasBattery = 1
					Else
						HasBattery = 2
					EndIf
					Exit
				EndIf
			EndIf
		Next
	EndIf
	
	If (Not IsNVGBlinking) Then RenderWorld(Tween)
	
	CurrTrisAmount = TrisRendered()
	
	If mpl\HasNTFGasmask = 2 And mpl\NightVisionEnabled Then ; ~ Show a HUD
		Color(255, 255, 255)
		
		SetFont fo\Font[Font_Digital_Large]
		
		Local PlusY% = 0
		
		PlusY = 40
		
		Text opt\GraphicWidth/2,(20+PlusY)*MenuScale,GetLocalString("Devices","nvg_refresh_1"),True,False
		Text opt\GraphicWidth/2,(60+PlusY)*MenuScale,Max(f2s(wbi\NightVisionTimer/60.0,1),0.0),True,False
		Text opt\GraphicWidth/2,(100+PlusY)*MenuScale,GetLocalString("Devices","nvg_refresh_2"),True,False
		
		Local Temp% = CreatePivot()
		Local Temp2% = CreatePivot()
		
		PositionEntity(Temp, EntityX(Collider), EntityY(Collider), EntityZ(Collider))
		
		Color(255, 255, 255)
		
		For np.NPCs = Each NPCs
			SetFont fo\Font[Font_Digital_Medium]
			If np\NVName <> "" And (Not np\HideFromNVG) Then ; ~ Don't waste your time if the string is empty
				PositionEntity(Temp2, np\NVX, np\NVY, np\NVZ)
				
				Local Dist# = EntityDistanceSquared(Temp2, Collider)
				
				If Dist < 552.25 Then ; ~ Don't draw text if the NPC is too far away
					PointEntity(Temp, Temp2)
					
					Local YawValue# = WrapAngle(EntityYaw(Camera) - EntityYaw(Temp))
					Local xValue# = 0.0
					
					If YawValue > 90.0 And YawValue <= 180.0 Then
						xValue = Sin(90.0) / 90.0 * YawValue
					ElseIf YawValue > 180 And YawValue < 270.0
						xValue = Sin(270.0) / YawValue * 270.0
					Else
						xValue = Sin(YawValue)
					EndIf
					
					Local PitchValue# = WrapAngle(EntityPitch(Camera) - EntityPitch(Temp))
					Local yValue# = 0.0
					
					If PitchValue > 90.0 And PitchValue <= 180.0 Then
						yValue = Sin(90.0) / 90.0 * PitchValue
					ElseIf PitchValue > 180.0 And PitchValue < 270
						yValue = Sin(270.0) / PitchValue * 270.0
					Else
						yValue = Sin(PitchValue)
					EndIf
					
					If (Not IsNVGBlinking) Then
						Text opt\GraphicWidth / 2 + xValue * (opt\GraphicWidth / 2),opt\GraphicHeight / 2 - yValue * (opt\GraphicHeight / 2),np\NVName,True,True
						Text opt\GraphicWidth / 2 + xValue * (opt\GraphicWidth / 2),opt\GraphicHeight / 2 - yValue * (opt\GraphicHeight / 2) + 30.0 * MenuScale,f2s(Dist,1)+" m",True,True
					EndIf
				EndIf
			EndIf
		Next
		
		FreeEntity(Temp)
		FreeEntity(Temp2)
		
		Color 0, 0, 55
	EndIf
	
	If HasBattery > 0 Then
		If wbi\NightVision = 2 Then ; ~ Show a HUD
			Color(255, 255, 255)
			
			SetFont fo\Font[Font_Digital_Large]
			
			PlusY% = 0
			
			If HasBattery = 1 Then PlusY = 40
			
			Text opt\GraphicWidth/2,(20+PlusY)*MenuScale,GetLocalString("Devices","nvg_refresh_1"),True,False
			Text opt\GraphicWidth/2,(60+PlusY)*MenuScale,Max(f2s(wbi\NightVisionTimer/60.0,1),0.0),True,False
			Text opt\GraphicWidth/2,(100+PlusY)*MenuScale,GetLocalString("Devices","nvg_refresh_2"),True,False
			
			Temp% = CreatePivot()
			Temp2% = CreatePivot()
			
			PositionEntity(Temp, EntityX(Collider), EntityY(Collider), EntityZ(Collider))
			
			Color(255, 255, 255)
			
			For np.NPCs = Each NPCs
				SetFont fo\Font[Font_Digital_Medium]
				If np\NVName <> "" And (Not np\HideFromNVG) Then ; ~ Don't waste your time if the string is empty
					PositionEntity(Temp2, np\NVX, np\NVY, np\NVZ)
					
					Dist# = EntityDistanceSquared(Temp2, Collider)
					
					If Dist < 552.25 Then ; ~ Don't draw text if the NPC is too far away
						PointEntity(Temp, Temp2)
						
						YawValue# = WrapAngle(EntityYaw(Camera) - EntityYaw(Temp))
						xValue# = 0.0
						
						If YawValue > 90.0 And YawValue <= 180.0 Then
							xValue = Sin(90.0) / 90.0 * YawValue
						ElseIf YawValue > 180 And YawValue < 270.0
							xValue = Sin(270.0) / YawValue * 270.0
						Else
							xValue = Sin(YawValue)
						EndIf
						
						PitchValue# = WrapAngle(EntityPitch(Camera) - EntityPitch(Temp))
						yValue# = 0.0
						
						If PitchValue > 90.0 And PitchValue <= 180.0 Then
							yValue = Sin(90.0) / 90.0 * PitchValue
						ElseIf PitchValue > 180.0 And PitchValue < 270
							yValue = Sin(270.0) / PitchValue * 270.0
						Else
							yValue = Sin(PitchValue)
						EndIf
						
						If (Not IsNVGBlinking) Then
							Text opt\GraphicWidth / 2 + xValue * (opt\GraphicWidth / 2),opt\GraphicHeight / 2 - yValue * (opt\GraphicHeight / 2),np\NVName,True,True
							Text opt\GraphicWidth / 2 + xValue * (opt\GraphicWidth / 2),opt\GraphicHeight / 2 - yValue * (opt\GraphicHeight / 2) + 30.0 * MenuScale,f2s(Dist,1)+" m",True,True
						EndIf
					EndIf
				EndIf
			Next
			
			FreeEntity(Temp)
			FreeEntity(Temp2)
			
			Color 0, 0, 55
		ElseIf wbi\NightVision = 1
			Color 0, 55, 0
		Else ; ~ SCRAMBLE
			Color 55, 55, 55
		EndIf
		For k = 0 To 10
			Rect 45,opt\GraphicHeight*0.5-(k*20),54,10,True
		Next
		If wbi\NightVision = 2
			Color 0, 0, 255
		ElseIf wbi\NightVision = 1
			Color 0, 255, 0
		Else ; ~ SCRAMBLE
			Color 255, 255, 255
		EndIf
		For l = 0 To Min(Floor((Power + 50) * 0.01), 11)
			Rect 45,opt\GraphicHeight*0.5-(l*20),54,10,True
		Next
		DrawImage NVGImages,40,opt\GraphicHeight*0.5+30,0
	EndIf
	
	; ~ Render sprites
	CameraProjMode Ark_Blur_Cam,2
	CameraProjMode Camera,0
	RenderWorld()
	CameraProjMode Ark_Blur_Cam,0
	
	If fps\Factor[0] > 0.0 Then
		If HasBattery = 1 And ((MilliSecs() Mod 800) < 400)
			Color 255, 0, 0
			SetFont fo\Font[Font_Digital_Large]
			
			Text opt\GraphicWidth/2,20*MenuScale,GetLocalString("Devices","nvg_low_bat"),True,False
			Color 255,255,255
		EndIf
	EndIf
End Function

Function ScaleRender(x#,y#,hscale#=1.0,vscale#=1.0)
	If Camera<>0 Then HideEntity Camera
	WireFrame 0
	ShowEntity Fresize_Image
	ScaleEntity Fresize_Image,hscale,vscale,1.0
	PositionEntity Fresize_Image, x, y, 1.0001
	ShowEntity Fresize_Cam
	RenderWorld()
	HideEntity Fresize_Cam
	HideEntity Fresize_Image
	WireFrame WireframeState
	If Camera<>0 Then ShowEntity Camera
End Function

Function InitFastResize()
	Local cam% = CreateCamera()
	CameraProjMode cam, 2
	CameraZoom cam, 0.1
	CameraClsMode cam, 0, 0
	CameraRange cam, 0.1, 1.5
	MoveEntity cam, 0, 0, -10000
	
	Fresize_Cam = cam
	
    ; ~ Create sprite
	Local spr% = CreateMesh(cam)
	Local sf% = CreateSurface(spr)
	AddVertex sf, -1, 1, 0, 0, 0
	AddVertex sf, 1, 1, 0, 1, 0
	AddVertex sf, -1, -1, 0, 0, 1
	AddVertex sf, 1, -1, 0, 1, 1
	AddTriangle sf, 0, 1, 2
	AddTriangle sf, 3, 2, 1
	EntityFX spr, 17
	ScaleEntity spr, 2048.0 / Float(RealGraphicWidth), 2048.0 / Float(RealGraphicHeight), 1
	PositionEntity spr, 0, 0, 1.0001
	EntityOrder spr, -100001
	EntityBlend spr, 1
	Fresize_Image = spr
	
    ; ~ Create texture
	Fresize_Texture = CreateTexture(4096, 4096, 1+256)
	Fresize_Texture2 = CreateTexture(4096, 4096, 1+256)
	TextureBlend Fresize_Texture2,3
	If Fresize_Texture2<>0 Then
		SetBuffer(TextureBuffer(Fresize_Texture2))
	EndIf
	ClsColor 0,0,0
	Cls
	SetBuffer(BackBuffer())
	EntityTexture spr, Fresize_Texture,0,0
	EntityTexture spr, Fresize_Texture2,0,1
	
	HideEntity Fresize_Cam
End Function

Function GammaUpdate()
	
	If opt\DisplayMode=1 Then
		If (RealGraphicWidth<>opt\GraphicWidth) Lor (RealGraphicHeight<>opt\GraphicHeight) Then
			SetBuffer TextureBuffer(Fresize_Texture)
			ClsColor 0,0,0 : Cls
			CopyRect 0,0,opt\GraphicWidth,opt\GraphicHeight,2048-opt\GraphicWidth/2,2048-opt\GraphicHeight/2,BackBuffer(),TextureBuffer(Fresize_Texture)
			SetBuffer BackBuffer()
			ClsColor 0,0,0 : Cls
			ScaleRender(0,0,4096.0 / Float(opt\GraphicWidth) * AspectRatioRatio, 4096.0 / Float(opt\GraphicWidth) * AspectRatioRatio)
		EndIf
	EndIf
	If ScreenGamma>=1.0 Then
		CopyRect 0,0,RealGraphicWidth,RealGraphicHeight,2048-RealGraphicWidth/2,2048-RealGraphicHeight/2,BackBuffer(),TextureBuffer(Fresize_Texture)
		EntityBlend Fresize_Image,1
		ClsColor 0,0,0 : Cls
		ScaleRender(-1.0/Float(RealGraphicWidth),1.0/Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth))
		EntityFX Fresize_Image,1+32
		EntityBlend Fresize_Image,3
		EntityAlpha Fresize_Image,ScreenGamma-1.0
		ScaleRender(-1.0/Float(RealGraphicWidth),1.0/Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth))
	ElseIf ScreenGamma<1.0 Then
		CopyRect 0,0,RealGraphicWidth,RealGraphicHeight,2048-RealGraphicWidth/2,2048-RealGraphicHeight/2,BackBuffer(),TextureBuffer(Fresize_Texture)
		EntityBlend Fresize_Image,1
		ClsColor 0,0,0 : Cls
		ScaleRender(-1.0/Float(RealGraphicWidth),1.0/Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth))
		EntityFX Fresize_Image,1+32
		EntityBlend Fresize_Image,2
		EntityAlpha Fresize_Image,1.0
		SetBuffer TextureBuffer(Fresize_Texture2)
		ClsColor 255*ScreenGamma,255*ScreenGamma,255*ScreenGamma
		Cls
		SetBuffer BackBuffer()
		ScaleRender(-1.0/Float(RealGraphicWidth),1.0/Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth))
		SetBuffer(TextureBuffer(Fresize_Texture2))
		ClsColor 0,0,0
		Cls
		SetBuffer(BackBuffer())
	EndIf
	EntityFX Fresize_Image,1
	EntityBlend Fresize_Image,1
	EntityAlpha Fresize_Image,1.0
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D