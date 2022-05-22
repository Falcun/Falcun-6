package falcun.net.util;

import net.minecraft.network.play.server.*;

public enum PacketTypePlayIn {

	USE_BED(S0APacketUseBed.class),
	ANIMATION(S0BPacketAnimation.class),
	SPAWN_PLAYER(S0CPacketSpawnPlayer.class),
	COLLECT_ITEM(S0DPacketCollectItem.class),
	SPAWN_OBJECT(S0EPacketSpawnObject.class),
	SPAWN_MOB(S0FPacketSpawnMob.class),
	KEEP_ALIVE(S00PacketKeepAlive.class),
	ENTITY_ATTACH(S1BPacketEntityAttach.class),
	ENTITY_METADATA(S1CPacketEntityMetadata.class),
	ENTITY_EFFECT(S1DPacketEntityEffect.class),
	REMOVE_ENTITY_EFFECT(S1EPacketRemoveEntityEffect.class),
	SET_EXPERIENCE(S1FPacketSetExperience.class),
	JOIN_GAME(S01PacketJoinGame.class),
	PARTICLES(S2APacketParticles.class),
	CHANGE_GAME_STATE(S2BPacketChangeGameState.class),
	SPAWN_GLOBAL_ENTITY(S2CPacketSpawnGlobalEntity.class),
	OPEN_WINDOW(S2DPacketOpenWindow.class),
	CLOSE_WINDOW(S2EPacketCloseWindow.class),
	SET_SLOT(S2FPacketSetSlot.class),
	CHAT(S02PacketChat.class),
	TAB_COMPLETE(S3APacketTabComplete.class),
	SCOREBOARD_OBJECTIVE(S3BPacketScoreboardObjective.class),
	UPDATE_SCORE(S3CPacketUpdateScore.class),
	DISPLAY_SCOREBOARD(S3DPacketDisplayScoreboard.class),
	TEAMS(S3EPacketTeams.class),
	CUSTOM_PAYLOAD(S3FPacketCustomPayload.class),
	TIME_UPDATE(S03PacketTimeUpdate.class),
	ENTITY_EQUIPMENT(S04PacketEntityEquipment.class),
	SPAWN_POSITION(S05PacketSpawnPosition.class),
	UPDATE_HEALTH(S06PacketUpdateHealth.class),
	RESPAWN(S07PacketRespawn.class),
	PLAYER_POS_LOOK(S08PacketPlayerPosLook.class),
	HELD_ITEM_CHANGE(S09PacketHeldItemChange.class),
	SPAWN_PAINTING(S10PacketSpawnPainting.class),
	SPAWN_EXPERIENCE_ORB(S11PacketSpawnExperienceOrb.class),
	ENTITY_VELOCITY(S12PacketEntityVelocity.class),
	DESTROY_ENTITIES(S13PacketDestroyEntities.class),
	ENTITY(S14PacketEntity.class),
	ENTITY_TELEPORT(S18PacketEntityTeleport.class),
	ENTITY_HEAD_LOOK(S19PacketEntityHeadLook.class),
	ENTITY_STATUS(S19PacketEntityStatus.class),
	ENTITY_PROPERTIES(S20PacketEntityProperties.class),
	CHUNK_DATA(S21PacketChunkData.class),
	MULTI_BLOCK_CHANGE(S22PacketMultiBlockChange.class),
	BLOCK_CHANGE(S23PacketBlockChange.class),
	BLOCK_ACTION(S24PacketBlockAction.class),
	BLOCK_BREAK_ANIM(S25PacketBlockBreakAnim.class),
	MAP_CHUNK_BULK(S26PacketMapChunkBulk.class),
	EXPLOSION(S27PacketExplosion.class),
	EFFECT(S28PacketEffect.class),
	SOUND_EFFECT(S29PacketSoundEffect.class),
	WINDOW_ITEMS(S30PacketWindowItems.class),
	WINDOW_PROPERTY(S31PacketWindowProperty.class),
	CONFIRM_TRANSACTION(S32PacketConfirmTransaction.class),
	UPDATE_SIGN(S33PacketUpdateSign.class),
	MAPS(S34PacketMaps.class),
	UPDATE_TILE_ENTITY(S35PacketUpdateTileEntity.class),
	SIGN_EDITOR_OPEN(S36PacketSignEditorOpen.class),
	STATISTICS(S37PacketStatistics.class),
	PLAYER_LIST_ITEM(S38PacketPlayerListItem.class),
	PLAYER_ABILITIES(S39PacketPlayerAbilities.class),
	DISCONNECT(S40PacketDisconnect.class),
	SERVER_DIFFICULTY(S41PacketServerDifficulty.class),
	COMBAT_EVENT(S42PacketCombatEvent.class),
	CAMERA(S43PacketCamera.class),
	WORLD_BORDER(S44PacketWorldBorder.class),
	TITLE(S45PacketTitle.class),
	SET_COMPRESSION_LEVEL(S46PacketSetCompressionLevel.class),
	PLAYER_LIST_HEADER_FOOTER(S47PacketPlayerListHeaderFooter.class),
	RESOURCE_PACK_SEND(S48PacketResourcePackSend.class),
	UPDATE_ENTITY_NBT(S49PacketUpdateEntityNBT.class),
	;

	private final Class<?> klass;

	PacketTypePlayIn(Class<?> klass) {
		this.klass = klass;
	}

	public static PacketTypePlayIn of(Class<?> klass) {
		if (klass == null) return null;
		for (PacketTypePlayIn value : PacketTypePlayIn.values()) {
			if (value.klass == klass) return value;
		}
		return null;
	}

	public Class<?> getClassType() {
		return klass;
	}
}
