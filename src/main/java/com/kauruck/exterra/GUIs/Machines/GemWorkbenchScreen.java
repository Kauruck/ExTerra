package com.kauruck.exterra.GUIs.Machines;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.Init.ModBlocks;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GemWorkbenchScreen extends ContainerScreen<GemWorkbenchContainer> {

    private ResourceLocation GUI = new ResourceLocation(ExTerra.MOD_ID, "textures/gui/gem_workbench.png");

    public GemWorkbenchScreen(GemWorkbenchContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
        String s = I18n.format(ModBlocks.GEM_WORKBENCH.getTranslationKey());
        font.drawString(ms, s, xSize / 2 - font.getStringWidth(s) / 2, 6, 4210752);
        font.drawString(ms, I18n.format("container.inventory"), 8, ySize - 110, 4210752);
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.func_230459_a_(ms, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(GUI);
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        blit(ms, k, l, 0, 0, xSize, ySize);
    }
}
