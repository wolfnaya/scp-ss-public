
Function FillRoom_Cont_173(r.Rooms)
	Local d.Doors,de.Decals,sc.SecurityCams
	Local i
	
	;the containment doors
	r\RoomDoors[1] = CreateDoor(r\zone, r\x,r\y, r\z + 1510.0 * RoomScale, 0, r, True, True)
	r\RoomDoors[1]\locked = False : r\RoomDoors[1]\AutoClose = False
	r\RoomDoors[1]\dir = 1 : r\RoomDoors[1]\open = True 
	FreeEntity(r\RoomDoors[1]\buttons[0]) : r\RoomDoors[1]\buttons[0] = 0
	FreeEntity(r\RoomDoors[1]\buttons[1]) : r\RoomDoors[1]\buttons[1] = 0
	r\RoomDoors[1]\MTFClose = False
	
	r\Objects[0] = LoadMesh_Strict("GFX\map\rooms\cont_173\cont_173_center.b3d",r\obj)
	EntityPickMode r\Objects[0],2
	EntityType r\Objects[0],HIT_MAP
	EntityFX r\Objects[0], 2
	LightMesh r\Objects[0],-120,-120,-120
	
	r\Objects[1] = LoadMesh_Strict("GFX\map\Props\alarm_cylinder.b3d");8 568.77263 1437.5494 ;450 90 270
	ScaleEntity r\Objects[1],RoomScale,RoomScale,RoomScale
	PositionEntity r\Objects[1],r\x+8*RoomScale,r\y+568.0*RoomScale,r\z+1437.0*RoomScale,True
	RotateEntity r\Objects[1],450,90,270
	EntityParent r\Objects[1],r\obj
	
	r\Objects[2] = LoadMesh_Strict("GFX\map\Props\alarm_rotor.b3d")
	ScaleEntity r\Objects[2],RoomScale,RoomScale,RoomScale
	PositionEntity r\Objects[2],r\x+8*RoomScale,r\y+568.0*RoomScale,r\z+1437.0*RoomScale,True
	RotateEntity r\Objects[2],450,90,270
	EntityParent r\Objects[2],r\obj
	
	r\Objects[3] = CreatePivot()
	PositionEntity r\Objects[3],r\x,r\y+5*RoomScale,r\z+2246*RoomScale,True
	EntityParent r\Objects[3],r\obj
	
	r\AlarmRotor[0] = r\Objects[2]
	r\AlarmRotorLight[0] = CreateLight(3,r\Objects[2])
	MoveEntity r\AlarmRotorLight[0],0,0,0.001
	LightRange r\AlarmRotorLight[0],1.5
	LightColor r\AlarmRotorLight[0],255*3,100*3,0
	RotateEntity r\AlarmRotorLight[0],0,0,45
	LightConeAngles r\AlarmRotorLight[0],0,75
	
End Function

Function UpdateEvent_Cont_173(e.Events)
	Local n.NPCs,i,angle#
	
	If PlayerRoom = e\room Then
		
		If e\SoundCHN = 0 Then
			e\SoundCHN = PlaySound_Strict(AlarmSFX[9])
		Else
			If Not ChannelPlaying(e\SoundCHN) Then e\SoundCHN = PlaySound_Strict(AlarmSFX[9])
		EndIf
		
;		HideEntity Overlay[0]
;		CameraFogRange Camera, 5,15
;		
;		angle = Max(Sin(EntityYaw(Collider)+90),0.0)
;		CameraFogColor (Camera,200+(angle*40),200+(angle*20),200)
;		CameraClsColor (Camera,200+(angle*40),200+(angle*20),200)
;		CameraRange(Camera, 0.01, 65)
;		
;		AmbientLight (200, 200, 200)
		
	EndIf
	
	If PlayerRoom = e\room Lor IsRoomAdjacent(e\room,PlayerRoom) Then
		UpdateAlarmRotor(e\room\AlarmRotor[0],4)
		ShowEntity e\room\AlarmRotor[0]
	Else
		HideEntity e\room\AlarmRotor[0]
	EndIf
	
	If (Not clm\DMode) Then
		
		If e\EventState[0] = 1 Then
			If EntityDistanceSquared(Curr173\Collider, e\room\Objects[3])>PowTwo(8.0) Then
				e\room\RoomDoors[1]\open = False
				Contained173 = True
				PlayAnnouncement("SFX\Character\MTF\Announc173Contain.ogg")
				e\EventState[0] = 2
			EndIf
		EndIf
		
	Else
		
		If e\EventState[0] = 0 Then
			If PlayerRoom = e\room Then
				
				If SelectedDifficulty\SaveType = SAVEONSCREENS Then
					CreateHintMsg(GetLocalString("Menu","hint_saveonscreens"))
				ElseIf SelectedDifficulty\SaveType = SAVEONQUIT Then
					CreateHintMsg(GetLocalString("Menu","hint_saveonquit"))
				Else
					CreateHintMsg(GetLocalStringR("Menu","hint_saveanywhere",KeyName[KEY_SAVE]))
				EndIf
				
				While e\room\RoomDoors[1]\openstate < 180
					e\room\RoomDoors[1]\openstate = Min(180, e\room\RoomDoors[1]\openstate + 0.8)
					MoveEntity(e\room\RoomDoors[1]\obj, Sin(e\room\RoomDoors[1]\openstate) / 180.0, 0, 0)
					MoveEntity(e\room\RoomDoors[1]\obj2, -Sin(e\room\RoomDoors[1]\openstate) / 180.0, 0, 0)
				Wend
				
				e\EventState[0] = 1
			EndIf
		Else
			
			If e\EventState[0] >= 500 Then
				e\EventState[0] = e\EventState[0]+fps\Factor[0]
				
				If e\EventState[1] = 0 Then
					CanSave = True
				EndIf
				
				;!EXPEREMENTAL!
				
				If e\EventState[0] > 900+3*70 And e\EventState[0] < 900+4*70 Then 
					CameraShake = 0.2
				ElseIf e\EventState[0] > 900+32.3*70 And e\EventState[0] < 900+34*70
					CameraShake = 0.4
				ElseIf e\EventState[0] > 900+51*70 And e\EventState[0] < 900+53.5*70
					CameraShake = 1.0
				ElseIf e\EventState[0] > 900+57.5*70 And e\EventState[0] < 900+58.5*70
					CameraShake = 0.4
				EndIf
				
				;!-----------!
				
			EndIf
			
			If (e\EventState[2]<11) Then
				If (Not ChannelPlaying(e\SoundCHN2)) Then
					e\EventState[2] = e\EventState[2]+1
					
					If (e\Sound2 <> 0) Then
						FreeSound_Strict(e\Sound2)
						e\Sound2 = 0
					EndIf
					
					e\Sound2 = LoadSound_Strict("SFX\Alarm\ContainmentBreach\Alarm2_"+Int(e\EventState[2])+".ogg")
					e\SoundCHN2 = PlaySound_Strict(e\Sound2)
				Else
					If Int(e\EventState[2]) = 8 Then CameraShake = 1.0
				EndIf
			EndIf
			
			If ((e\EventState[0] Mod 600 > 300) And ((e\EventState[0]+fps\Factor[0]) Mod 600 < 300)) Then
				i = Floor((e\EventState[0]-5000)/600)+1
				
				If i = 0 Then PlaySound_Strict(LoadTempSound("SFX\Intercom\scripted\scripted6.ogg"))
				
				If (i>26) Then
					
					DebugLog "delete alarm"
					
					RemoveEvent(e)							
				EndIf
			EndIf					
		End If
		;[End Block]
		
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D