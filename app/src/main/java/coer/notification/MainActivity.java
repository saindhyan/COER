package coer.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import coer.notification.ui.notice.notice_fragment;
import coer.notification.ui.tools.tools_fragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.frame);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_drower);



        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        mAuth = FirebaseAuth.getInstance();


        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();

        MenuItem tools = menu.findItem(R.id.communicate);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                finish();

                break;
        }
        switch (item.getItemId()) {
            case R.id.navigation_website:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://incubator.coer.ac.in/"));
                startActivity(browserIntent);
                drawerLayout.closeDrawers();
                break;
        }
        switch (item.getItemId()) {
            case R.id.navigation_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String sharingbody = "Hi, i found this amazing college application whare you will find all the contnt related to graduation and college related info " +
                        " here is the link https://incubator.coer.ac.in/";
                String sharingsubject = "Coer'A College App";

                sharingIntent.putExtra(Intent.EXTRA_TEXT, sharingbody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, sharingsubject);

                startActivity(Intent.createChooser(sharingIntent, "share using"));
                drawerLayout.closeDrawers();
                break;
        }
        switch (item.getItemId()) {
            case R.id.navigation_ContectUs:
                String[] TO_EMAILS = {"piyushsaini597@gmail.com"};
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, TO_EMAILS);


                startActivity(Intent.createChooser(intent, "send a mail"));
                drawerLayout.closeDrawers();

                break;
        }
        switch (item.getItemId()) {
            case R.id.navigation_developer:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                        new developer()).commit();
                bottomNavigationView.setVisibility(View.INVISIBLE);

                drawerLayout.closeDrawers();

                break;
        }
        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(getApplicationContext(), "You Are Logged out", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, logincategory.class));
                finish();
                drawerLayout.closeDrawers();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
                     finishAffinity();
        }


        }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, logincategory.class));
        }
    }
}