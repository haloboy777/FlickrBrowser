package me.haloboy777.flickerbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateToolbar(true);
        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);
        if (photo!=null){
            TextView photoTitle = (TextView) findViewById(R.id.photo_title);
            TextView photoTags = (TextView) findViewById(R.id.photo_tags);
            TextView photoAuthor = (TextView) findViewById(R.id.photo_tags);

//            photoTitle.setText("Title: "+photo.getTitle());
//            photoTags.setText("Tags: "+photo.getTags());
            Resources resources = getResources();
            String text = resources.getString(R.string.photo_title_text, photo.getTitle());

            photoTags.setText(resources.getString(R.string.photo_tags_text, photo.getTags()));
            photoTitle.setText(text);
            photoAuthor.setText(photo.getAuthor());

            ImageView photoImage = (ImageView) findViewById(R.id.photo_image);
            Picasso.with(this).load(photo.getLink())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(photoImage);
        }
    }

}
