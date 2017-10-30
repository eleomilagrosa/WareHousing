package com.fusiotec.warehousing.warehousing.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Fonts {

    /*
	 * This class is use to configure the fonts for all the views in the application
	 */

    //Font Styles
    public static final int SEGOE_UI = 0;
    public static final int SEGOE_UI_BOLD = 1;
    public static final int SEGOE_UI_ITALIC = 2;
    public static final int SEGOE_UI_LIGHT = 3;
    public static final int SEGOE_UI_SEMI_LIGHT = 4;
    public static final int SEGOE_UI_BOLD_ITALIC = 5;
    public static final int SEGOE_UI_BLACK = 6;
    public static final int SEGOE_UI_BLACK_ITALIC = 7;
    public static final int SEGOE_UI_LIGHT_ITALIC = 8;
    public static final int SEGOE_UI_SEMI_BOLD = 9;
    public static final int SEGOE_UI_SEMI_BOLD_ITALIC = 10;
    public static final int SEGOE_UI_SEMI_LIGHT_ITALIC = 11;
    public static final int DS_DIGI = 12;
    public static final int DS_DIGI_BOLD = 13;
    public static final int DS_DIGI_ITALIC = 14;
    public static final int DS_DIGI_ITALIC_BOLD = 15;

    public static final int GOTHAM_BLACK = 16;
    public static final int GOTHAM_BLACKLTA = 17;
    public static final int GOTHAM_BOLD = 18;
    public static final int GOTHAM_BOLDLTA = 19;
    public static final int GOTHAM_BOOK = 20;
    public static final int GOTHAM_BOOKLTA = 21;
    public static final int GOTHAM_LIGHT = 22;
    public static final int GOTHAM_LIGHTLTA = 23;
    public static final int GOTHAM_MEDIUM = 24;
    public static final int GOTHAM_MEDIUMLTA = 25;
    public static final int GOTHAM_THIN = 26;
    public static final int GOTHAM_THINLTA = 27;
    public static final int GOTHAM_ULTRA = 28;
    public static final int GOTHAM_ULTRALTA = 29;
    public static final int GOTHAM_XLIGHT = 30;
    public static final int GOTHAM_XLIGHTA = 31;
    public static final int BEBAS__ = 32;
    public static final int AMSDAM_REGULAR_MAC = 33;
    public static final int GREATEVIBES = 34;
    public static final int LOBSTER = 35;
    public static final int PRIMER = 36;
    public static final int PRIMER_BOLD = 37;

    public static final int HTK_BOLD = 38;
    public static final int HTK_NORMAL = 39;
    public static final int HTK_ROMAN = 40;

    public static final int RECEIPT_FONT = 41;
    public static final int ROBOTOCONDENSED_LIGHT = 42;
    public static final int ROBOTO_REGULAR = 43;


    private Context context;

    public Fonts(Context context){
        this.context = context;
    }

    public Typeface getTypeFace(int type){
        switch(type){
            case SEGOE_UI: return Typeface.createFromAsset(context.getAssets(),"fonts/segoeui.ttf");
            case SEGOE_UI_BOLD: return Typeface.createFromAsset(context.getAssets(),"fonts/segoeuib.ttf");
            case SEGOE_UI_ITALIC: return Typeface.createFromAsset(context.getAssets(),"fonts/segoeuii.ttf");
            case SEGOE_UI_LIGHT: return Typeface.createFromAsset(context.getAssets(),"fonts/segoeuil.ttf");
            case SEGOE_UI_SEMI_LIGHT: return Typeface.createFromAsset(context.getAssets(),"fonts/segoeuisl.ttf");
            case SEGOE_UI_BOLD_ITALIC: return Typeface.createFromAsset(context.getAssets(),"fonts/segoeuiz.ttf");
            case SEGOE_UI_BLACK: return Typeface.createFromAsset(context.getAssets(),"fonts/seguibl.ttf");
            case SEGOE_UI_BLACK_ITALIC: return Typeface.createFromAsset(context.getAssets(),"fonts/seguibli.ttf");
            case SEGOE_UI_LIGHT_ITALIC: return Typeface.createFromAsset(context.getAssets(),"fonts/seguili.ttf");
            case SEGOE_UI_SEMI_BOLD: return Typeface.createFromAsset(context.getAssets(),"fonts/seguisb.ttf");
            case SEGOE_UI_SEMI_BOLD_ITALIC: return Typeface.createFromAsset(context.getAssets(),"fonts/segoeuil.ttf");
            case SEGOE_UI_SEMI_LIGHT_ITALIC: return Typeface.createFromAsset(context.getAssets(),"fonts/seguisli.ttf");
            case DS_DIGI: return Typeface.createFromAsset(context.getAssets(),"fonts/DS-DIGI.TTF");
            case DS_DIGI_BOLD: return Typeface.createFromAsset(context.getAssets(),"fonts/DS-DIGIB.TTF");
            case DS_DIGI_ITALIC: return Typeface.createFromAsset(context.getAssets(),"fonts/DS-DIGII.TTF");
            case DS_DIGI_ITALIC_BOLD: return Typeface.createFromAsset(context.getAssets(),"fonts/DS-DIGIT.TTF");
            case GOTHAM_BLACK: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-Black.otf");
            case GOTHAM_BLACKLTA: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-BlackIta.otf");
            case GOTHAM_BOLD: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-Bold.otf");
            case GOTHAM_BOLDLTA: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-BoldIta.otf");
            case GOTHAM_BOOK: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-Book.otf");
            case GOTHAM_BOOKLTA: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-BookIta.otf");
            case GOTHAM_LIGHT: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-Light.otf");
            case GOTHAM_LIGHTLTA: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-LightIta.otf");
            case GOTHAM_MEDIUM: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-Medium.otf");
            case GOTHAM_MEDIUMLTA: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-MediumIta.otf");
            case GOTHAM_THIN: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-Thin.otf");
            case GOTHAM_THINLTA: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-ThinIta.otf");
            case GOTHAM_ULTRA: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-Ultra.otf");
            case GOTHAM_ULTRALTA: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-UltraIta.otf");
            case GOTHAM_XLIGHT: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-XLight.otf");
            case GOTHAM_XLIGHTA: return Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-XLightIta.otf");
            case BEBAS__: return Typeface.createFromAsset(context.getAssets(),"fonts/BEBAS__.TTF");
            case AMSDAM_REGULAR_MAC: return Typeface.createFromAsset(context.getAssets(),"fonts/Amsdam_Regular_Mac.ttf");
            case GREATEVIBES: return Typeface.createFromAsset(context.getAssets(),"fonts/GreatVibes-Regular.ttf");
            case LOBSTER: return Typeface.createFromAsset(context.getAssets(),"fonts/Lobster.otf");
            case PRIMER: return Typeface.createFromAsset(context.getAssets(),"fonts/primer.ttf");
            case PRIMER_BOLD: return Typeface.createFromAsset(context.getAssets(),"fonts/primer_bold.ttf");
            case HTK_BOLD: return Typeface.createFromAsset(context.getAssets(),"fonts/htk_bold.ttf");
            case HTK_NORMAL: return Typeface.createFromAsset(context.getAssets(),"fonts/htk_normal.ttf");
            case HTK_ROMAN: return Typeface.createFromAsset(context.getAssets(),"fonts/htk_roman.otf");
            case RECEIPT_FONT: return Typeface.createFromAsset(context.getAssets(),"fonts/cour.ttf");
            case ROBOTOCONDENSED_LIGHT: return Typeface.createFromAsset(context.getAssets(),"fonts/RobotoCondensed-Light.ttf");
            case ROBOTO_REGULAR: return Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Regular.ttf");
        }
        return null;
    }
    public static String getTypeFaceDir(int type){
        switch(type){
            case SEGOE_UI: return "fonts/segoeui.ttf";
            case SEGOE_UI_BOLD: return "fonts/segoeuib.ttf";
            case SEGOE_UI_ITALIC: return "fonts/segoeuii.ttf";
            case SEGOE_UI_LIGHT: return "fonts/segoeuil.ttf";
            case SEGOE_UI_SEMI_LIGHT: return "fonts/segoeuisl.ttf";
            case SEGOE_UI_BOLD_ITALIC: return "fonts/segoeuiz.ttf";
            case SEGOE_UI_BLACK: return "fonts/seguibl.ttf";
            case SEGOE_UI_BLACK_ITALIC: return "fonts/seguibli.ttf";
            case SEGOE_UI_LIGHT_ITALIC: return "fonts/seguili.ttf";
            case SEGOE_UI_SEMI_BOLD: return "fonts/seguisb.ttf";
            case SEGOE_UI_SEMI_BOLD_ITALIC: return "fonts/segoeuil.ttf";
            case SEGOE_UI_SEMI_LIGHT_ITALIC: return "fonts/seguisli.ttf";
            case DS_DIGI: return "fonts/DS-DIGI.TTF";
            case DS_DIGI_BOLD: return "fonts/DS-DIGIB.TTF";
            case DS_DIGI_ITALIC: return "fonts/DS-DIGII.TTF";
            case DS_DIGI_ITALIC_BOLD: return "fonts/DS-DIGIT.TTF";
            case GOTHAM_BLACK: return "fonts/Gotham-Black.otf";
            case GOTHAM_BLACKLTA: return "fonts/Gotham-Blacklta.otf";
            case GOTHAM_BOLD: return "fonts/Gotham-Bold.otf";
            case GOTHAM_BOLDLTA: return "fonts/Gotham-Boldlta.otf";
            case GOTHAM_BOOK: return "fonts/Gotham-Book.otf";
            case GOTHAM_BOOKLTA: return "fonts/Gotham-Booklta.otf";
            case GOTHAM_LIGHT: return "fonts/Gotham-Light.otf";
            case GOTHAM_LIGHTLTA: return "fonts/Gotham-Lightlta.otf";
            case GOTHAM_MEDIUM: return "fonts/Gotham-Medium.otf";
            case GOTHAM_MEDIUMLTA: return "fonts/Gotham-Mediumlta.otf";
            case GOTHAM_THIN: return "fonts/Gotham-Thin.otf";
            case GOTHAM_THINLTA: return "fonts/Gotham-Thinlta.otf";
            case GOTHAM_ULTRA: return "fonts/Gotham-Ultra.otf";
            case GOTHAM_ULTRALTA: return "fonts/Gotham-Ultralta.otf";
            case GOTHAM_XLIGHT: return "fonts/Gotham-XLight.otf";
            case GOTHAM_XLIGHTA: return "fonts/Gotham-XLightlta.otf";
            case BEBAS__: return "fonts/BEBAS__.TTF";
            case AMSDAM_REGULAR_MAC: return "fonts/Amsdam_Regular_Mac.ttf";
            case GREATEVIBES: return "fonts/GreatVibes-Regular.ttf";
            case LOBSTER: return "fonts/Lobster.otf";
            case PRIMER: return "fonts/primer.ttf";
            case PRIMER_BOLD: return "fonts/primer_bold.ttf";
            case HTK_BOLD: return "fonts/htk_bold.ttf";
            case HTK_NORMAL: return "fonts/htk_normal.ttf";
            case HTK_ROMAN: return "fonts/htk_roman.otf";
            case RECEIPT_FONT: return "fonts/cour.ttf";
            case ROBOTOCONDENSED_LIGHT: return "fonts/RobotoCondensed-Light.ttf";
            case ROBOTO_REGULAR: return "fonts/Roboto-Regular.ttf";
        }
        return null;
    }

    public void setTypeFace(TextView view, int typeface){view.setTypeface(getTypeFace(typeface));}

    public void setTypeFace(EditText view, int typeface){view.setTypeface(getTypeFace(typeface));}

    public void setTypeFace(Button view, int typeface){
        view.setTypeface(getTypeFace(typeface));
    }

    public static void setTextSize(Context context, TextView view, int fontsize){
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimensionPixelSize(fontsize));
    }

    public static void setTextSize(Context context, EditText view, int fontsize){
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimensionPixelSize(fontsize));
    }

    public static void setTextSize(Context context, Button view, int fontsize){
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimensionPixelSize(fontsize));
    }

}
