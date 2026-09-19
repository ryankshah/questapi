package com.ryankshah.questapi.impl.persistence;

import com.mojang.serialization.Codec;
import com.ryankshah.questapi.QuestApi;
import com.ryankshah.questapi.api.quest.PlayerQuestData;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Server-wide persistent storage for every player's quest progress, attached to the server via
 * {@code MinecraftServer#getDataStorage()} rather than to any single dimension so that progress is
 * independent of which world a player last stood in.
 * <p>
 * There is no vanilla data-fixer schema for third-party mod data; {@link DataFixTypes#LEVEL} is used
 * as the least intrusive generic option, since our NBT structure shares no field paths with the
 * vanilla level schema and is therefore left untouched by its fixers.
 */
public final class QuestSavedData extends SavedData {

    public static final Codec<QuestSavedData> CODEC = PlayerQuestData.CODEC.listOf().xmap(
            QuestSavedData::fromList,
            data -> List.copyOf(data.players.values())
    );

    public static final SavedDataType<QuestSavedData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(QuestApi.MOD_ID, "player_data"),
            QuestSavedData::new,
            CODEC,
            DataFixTypes.LEVEL
    );

    private final Map<UUID, PlayerQuestData> players;

    public QuestSavedData() {
        this(new HashMap<>());
    }

    private QuestSavedData(Map<UUID, PlayerQuestData> players) {
        this.players = players;
    }

    private static QuestSavedData fromList(List<PlayerQuestData> list) {
        Map<UUID, PlayerQuestData> map = new HashMap<>();
        for (PlayerQuestData data : list) {
            map.put(data.playerId(), data);
        }
        return new QuestSavedData(map);
    }

    public PlayerQuestData getOrCreate(UUID playerId) {
        return players.computeIfAbsent(playerId, PlayerQuestData::empty);
    }

    public void markDirty() {
        setDirty();
    }
}
