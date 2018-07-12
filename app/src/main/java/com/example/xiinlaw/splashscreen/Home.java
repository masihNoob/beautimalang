//Dasbor

package com.example.xiinlaw.splashscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    public void pindah(View view) {

        Intent intent=new Intent(this,PetaGelintung.class);
        startActivity(intent);

    }
    public void pindah3(View view) {

        Intent intent=new Intent(this,Slader3d.class);
        startActivity(intent);

    }

    public void pindah4(View view) {

        Intent intent=new Intent(this,PetaKampungWarnaWarni.class);
        startActivity(intent);

    }
    public void pindah5(View view) {

        Intent intent=new Intent(this,PetaKayuTangan.class);
        startActivity(intent);

    }
    public void NextMethod(View view) {
        switch (view.getId()){
            case R.id.buttonGelintung:{
                Common.setLatGreen("-7.946027");
                Common.setLngGreen("112.638604");
                Common.setPlaceName("Go Green Glintung");
            }
            break;
            case R.id.buttonTridi:{
                Common.setLatGreen("-7.981849");
                Common.setLngGreen("112.6359163");
                Common.setPlaceName("Kampung Tridi");
            }
            break;
            case R.id.buttonKayutangan:{
                Common.setLatGreen("-7.9814255");
                Common.setLngGreen("112.628141");
                Common.setPlaceName("kajoetangan heritage");
            }
            break;case R.id.buttonWarna:{
                Common.setLatGreen("-7.9832215");
                Common.setLngGreen("112.6354428");
                Common.setPlaceName("warna warni jodipan");
            }
            break;
        }
        startActivity(new Intent(this, MapsActivity.class));
    }
}
