package com.wolski_msk.collector.sample.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wolski_msk.collector.sample.R;

import java.util.Arrays;
import java.util.List;

public class ListItemAdapter extends BaseAdapter{
    String [] result;
    Context context;


    public ListItemAdapter(Context  mainActivity, String[] prgmNameList) {

        result=prgmNameList;
        context=mainActivity;
    }
    @Override
    public int getCount() {

        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView nameSurname;
        ImageView nameImage;
        TextView title;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView ==null) {


            Holder holder = new Holder();


            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.single_item, parent, false);

           // Typeface typeFace = initFont("fonts/avenir/AvenirLTStd-Light.otf");

            holder.title = (TextView)convertView.findViewById(R.id.title);



            holder.nameSurname = (TextView) convertView.findViewById(R.id.nameSurname);
           // holder.nameSurname.setTypeface(typeFace);
            holder.nameSurname.setText(result[position]);


            holder.nameImage = (ImageView) convertView.findViewById(R.id.imageView1);
            String[] x = holder.nameSurname.getText().toString().split(" ");

            if (x.length > 1) {
                holder.nameImage.setImageBitmap(generateCircleBitmap(this.context, getMaterialColor(convertView), 20, String.valueOf(x[0].charAt(0)) + '.' + x[1].charAt(0) + '.'));
            } else if (x[0].length() > 1)
                holder.nameImage.setImageBitmap(generateCircleBitmap(this.context, getMaterialColor(convertView), 20, String.valueOf(x[0].charAt(0)) + x[0].charAt(1) + '.'));


            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
                }
            });
        }
        return convertView;
    }

    private Typeface initFont(String dir)
    {
       return Typeface.createFromAsset(context.getAssets(), dir);
    }


    private void initTitle() {

    }

    private void initNameSurname(){}


    private void initDate(){}

    private void initDescription(){}

    private void initProfileImage(){}

    private void initObjectPhoto(){}

    private void initMoneyAmount(){}


    private static List<Integer> materialColors = Arrays.asList(
            0xffe57373,
            0xfff06292,
            0xffba68c8,
            0xff9575cd,
            0xff7986cb,
            0xff64b5f6,
            0xff4fc3f7,
            0xff4dd0e1,
            0xff4db6ac,
            0xff81c784,
            0xffaed581,
            0xffff8a65,
            0xffd4e157,
            0xffffd54f,
            0xffffb74d,
            0xffa1887f,
            0xff90a4ae
    );

    public static int getMaterialColor(Object key) {
        return materialColors.get(Math.abs(key.hashCode()) % materialColors.size());
    }

    public static Bitmap generateCircleBitmap(Context context, int circleColor, float diameterDP, String text){
        final int textColor = 0xffffffff;

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float diameterPixels = diameterDP * (metrics.densityDpi / 160f);
        float radiusPixels = diameterPixels/2;

        // Create the bitmap
        Bitmap output = Bitmap.createBitmap((int) diameterPixels, (int) diameterPixels,
                Bitmap.Config.ARGB_8888);

        // Create the canvas to draw on
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);


        final Paint paintC = new Paint();
        paintC.setAntiAlias(true);
        paintC.setColor(circleColor);
        canvas.drawCircle(radiusPixels, radiusPixels, radiusPixels, paintC);

        if (text != null && text.length() > 0) {
            final Paint paintT = new Paint();
            paintT.setColor(textColor);
            paintT.setAntiAlias(true);
            paintT.setTextSize(radiusPixels * 3/4 );
            Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/avenir/AvenirLTStd-HeavyOblique.otf");
            paintT.setTypeface(typeFace);
            final Rect textBounds = new Rect();
            paintT.getTextBounds(text, 0, text.length(), textBounds);
            canvas.drawText(text, radiusPixels - textBounds.exactCenterX(), radiusPixels - textBounds.exactCenterY(), paintT);
        }

        return output;
    }

}

