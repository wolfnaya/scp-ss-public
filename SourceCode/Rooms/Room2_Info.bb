Function FillRoom_Room2_Info(r.Rooms)
	Local it.Items
	
	it = CreateItem("First Aid Kit", "firstaid",r\x-1116*RoomScale,r\y+50*RoomScale,r\z-51*RoomScale)
	EntityParent it\Collider, r\obj
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D