package net.minecraft.client.network;

import com.google.common.base.Objects;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;

public class NetworkPlayerInfo {

    private final GameProfile gameProfile;
    private WorldSettings.GameType gameType;
    private int responseTime;
    private boolean playerTexturesLoaded = false;
    private ResourceLocation locationSkin;
    private ResourceLocation locationCape;
    private String skinType;
    private IChatComponent displayName;
    private int field_178873_i = 0;
    private int field_178870_j = 0;
    private long field_178871_k = 0L;
    private long field_178868_l = 0L;
    private long field_178869_m = 0L;

    public NetworkPlayerInfo(S38PacketPlayerListItem.AddPlayerData playerData) {
        this.gameProfile = playerData.getProfile();
        this.gameType = playerData.getGameMode();
        this.responseTime = playerData.getPing();
        this.displayName = playerData.getDisplayName();
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public WorldSettings.GameType getGameType() {
        return this.gameType;
    }

    public int getResponseTime() {
        return this.responseTime;
    }

    protected void setGameType(WorldSettings.GameType gameType) {
        this.gameType = gameType;
    }

    protected void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public boolean hasLocationSkin() {
        return this.locationSkin != null;
    }

    public String getSkinType() {
        return this.skinType == null ? DefaultPlayerSkin.getSkinType(gameProfile.getId()) : skinType;
    }

    public ResourceLocation getLocationSkin() {
        if (locationSkin == null) {
            loadPlayerTextures();
        }

        return Objects.firstNonNull(locationSkin, DefaultPlayerSkin.getDefaultSkin(gameProfile.getId()));
    }

    public ResourceLocation getLocationCape() {
        if (locationCape == null) {
            loadPlayerTextures();
        }

        return locationCape;
    }

    public ScorePlayerTeam getPlayerTeam() {
        return Minecraft.getMinecraft().theWorld.getScoreboard().getPlayersTeam(getGameProfile().getName());
    }

    protected void loadPlayerTextures() {
        synchronized (this) {
            if (!playerTexturesLoaded) {
                playerTexturesLoaded = true;

                Minecraft.getMinecraft().getSkinManager().loadProfileTextures(this.gameProfile, (p_180521_1_, location, profileTexture) -> {
                    switch (p_180521_1_) {
                        case SKIN:
                            NetworkPlayerInfo.this.locationSkin = location;
                            NetworkPlayerInfo.this.skinType = profileTexture.getMetadata("model");

                            if (NetworkPlayerInfo.this.skinType == null) {
                                NetworkPlayerInfo.this.skinType = "default";
                            }

                            break;

                        case CAPE:
                            NetworkPlayerInfo.this.locationCape = location;
                    }
                }, true);
            }
        }
    }

    public void setDisplayName(IChatComponent displayNameIn) {
        this.displayName = displayNameIn;
    }

    public IChatComponent getDisplayName() {
        return this.displayName;
    }

    public int func_178835_l()
    {
        return this.field_178873_i;
    }

    public void func_178836_b(int p_178836_1_)
    {
        this.field_178873_i = p_178836_1_;
    }

    public int func_178860_m()
    {
        return this.field_178870_j;
    }

    public void func_178857_c(int p_178857_1_)
    {
        this.field_178870_j = p_178857_1_;
    }

    public long func_178847_n()
    {
        return this.field_178871_k;
    }

    public void func_178846_a(long p_178846_1_)
    {
        this.field_178871_k = p_178846_1_;
    }

    public long func_178858_o()
    {
        return this.field_178868_l;
    }

    public void func_178844_b(long p_178844_1_)
    {
        this.field_178868_l = p_178844_1_;
    }

    public long func_178855_p()
    {
        return this.field_178869_m;
    }

    public void func_178843_c(long p_178843_1_)
    {
        this.field_178869_m = p_178843_1_;
    }
}
