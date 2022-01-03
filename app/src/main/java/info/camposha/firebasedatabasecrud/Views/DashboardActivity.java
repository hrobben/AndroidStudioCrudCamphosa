
package info.camposha.firebasedatabasecrud.Views;


import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import info.camposha.firebasedatabasecrud.Helpers.Utils;
import info.camposha.firebasedatabasecrud.R;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class DashboardActivity extends AppCompatActivity {

    //We have 4 cards in the dashboard
    private LinearLayout viewScientistsCard,addScientistCard,third,closeCard;

    /**
     * Let's initialize our cards  and listen to their click events
     */
    private void initializeWidgets(){
        viewScientistsCard = findViewById(R.id.viewScientistsCard);
        addScientistCard = findViewById(R.id.addScientistCard);
        third = findViewById(R.id.third);
        closeCard = findViewById(R.id.closeCard);

        viewScientistsCard.setOnClickListener(v -> Utils.openActivity(DashboardActivity.this,
        ScientistsActivity.class));
        addScientistCard.setOnClickListener(v -> Utils.openActivity(DashboardActivity.this,
        CRUDActivity.class));
        third.setOnClickListener(v -> Utils.showInfoDialog(DashboardActivity.this, "YEEES",
        "Hey You can Display another page when this is clicked"));
        closeCard.setOnClickListener(v -> finishAffinity());
    }
    /**
     * Let's override the attachBaseContext() method so that custom fonts can
     * be used here as well
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    /**
     * When the back button is pressed finish this activity
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }

    /**
     * Let's override the onCreate() and call our initializeWidgets()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        this.initializeWidgets();
    }
}
//end