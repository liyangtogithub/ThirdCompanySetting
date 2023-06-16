package com.landsem.setting.view;

import com.landsem.setting.effect.BaseEffects;
import com.landsem.setting.effect.FadeIn;
import com.landsem.setting.effect.Fall;
import com.landsem.setting.effect.FlipH;
import com.landsem.setting.effect.FlipV;
import com.landsem.setting.effect.NewsPaper;
import com.landsem.setting.effect.RotateBottom;
import com.landsem.setting.effect.RotateLeft;
import com.landsem.setting.effect.Shake;
import com.landsem.setting.effect.SideFall;
import com.landsem.setting.effect.SlideBottom;
import com.landsem.setting.effect.SlideLeft;
import com.landsem.setting.effect.SlideRight;
import com.landsem.setting.effect.SlideTop;
import com.landsem.setting.effect.Slit;

public enum Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(RotateBottom.class),
    RotateLeft(RotateLeft.class),
    Slit(Slit.class),
    Shake(Shake.class),
    Sidefill(SideFall.class);
    
	@SuppressWarnings("rawtypes")
	private Class effectsClass;

    @SuppressWarnings("rawtypes")
	private Effectstype(Class effectsClass) {
        this.effectsClass = effectsClass;
    }

    public BaseEffects getAnimator() {
        try {
            return (BaseEffects) effectsClass.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
