package com.diazmiranda.juanjose.mibebe;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.IdRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.diazmiranda.juanjose.mibebe.fragments.AcercaDeFragment;
import com.diazmiranda.juanjose.mibebe.fragments.CartillaVacunacionFragment;
import com.diazmiranda.juanjose.mibebe.fragments.CrecimientoFragment;
import com.diazmiranda.juanjose.mibebe.fragments.PediatraFragment;
import com.diazmiranda.juanjose.mibebe.fragments.HistoryFragment;
import com.diazmiranda.juanjose.mibebe.fragments.LibroFragment;
import com.diazmiranda.juanjose.mibebe.fragments.PrincipalFragment;
import com.diazmiranda.juanjose.mibebe.fragments.ProfileFragment;
import com.diazmiranda.juanjose.mibebe.fragments.RegisterFragment;
import com.diazmiranda.juanjose.mibebe.fragments.VideosFragment;
import com.diazmiranda.juanjose.mibebe.requests.Model.UsuarioService;
import com.diazmiranda.juanjose.mibebe.requests.RetrofitRequest;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.Dependiente;
import com.diazmiranda.juanjose.mibebe.util.Constants;
import com.diazmiranda.juanjose.mibebe.util.FragmentInteraction;
import com.diazmiranda.juanjose.mibebe.util.SharedPreferencesHelper;
import com.diazmiranda.juanjose.mibebe.util.UI;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentInteraction.OnFragmentInteractionListener, View.OnClickListener {
    private final static String TAG = "MainActivity";
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private MenuItem searchItem;

    private int menuOptionId = 0;
    private boolean isMenuOptionChecked = true;

    private VIEWS current;

    private SharedPreferencesHelper sharedPreferences;
    private ProgressDialog progressDialog;

    private enum VIEWS { CRECIMIENTO, PEDIATRAS, VACUNAS}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startComponents();
        startDrawerComponents();

        if(Constants.currentBaby == null)
            mostrarBebe();


        handleIntent(getIntent());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    public void startDrawerComponents() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = new SharedPreferencesHelper(this);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isMenuOptionChecked = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (isMenuOptionChecked) return;

                Fragment fragment = null;
                Log.i("Main", "itemid: " + menuOptionId);
                searchItem.setVisible(false);

                showFab(false);
                switch (menuOptionId) {
                    case R.id.nav_home:
                        showFab(false);
                        fragment = new PrincipalFragment();
                        break;
                    case R.id.nav_register:
                        fragment = new RegisterFragment();
                        break;
                    case R.id.nav_doctors:
                        //searchItem.setVisible(true);
                        fab.setImageResource(R.drawable.ic_next);
                        current = VIEWS.PEDIATRAS;
                        fragment = new PediatraFragment();
                        break;
                    case R.id.nav_profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.nav_history:
                        //searchItem.setVisible(true);
                        fragment = new HistoryFragment();
                        break;
                    case R.id.nav_videos:
                        //searchItem.setVisible(true);
                        //fragment = new VideoFragment();
                        //fragment = new YoutubePlayerFragment();
                        fragment = new VideosFragment();
                        break;
                    case R.id.nav_crecimiento:
                        if(Constants.currentBaby == null) {
                            Toast.makeText( MainActivity.this, "Selecciona un dependiente", Toast.LENGTH_SHORT ).show();
                            return;
                        }
                        fragment = new CrecimientoFragment();
                        fab.setImageResource(R.drawable.ic_add);
                        showFab(true);
                        current = VIEWS.CRECIMIENTO;
                        break;
                    case R.id.nav_cartilla:
                        if(Constants.currentBaby == null) {
                            Toast.makeText( MainActivity.this, "Selecciona un dependiente", Toast.LENGTH_SHORT ).show();
                            return;
                        }
                        fragment = new CartillaVacunacionFragment();
                        current = VIEWS.VACUNAS;
                        fab.setImageResource(R.drawable.ic_add);
                        showFab(true);
                        break;
                    case R.id.nav_desarrollo:
                        fragment = new LibroFragment();
                        break;
                    case R.id.nav_logout:
                        enviarDatos();
                        break;
                    case R.id.Acerda_de:
                        fragment = new AcercaDeFragment();
                        break;
                    case R.id.nav_call_center:
                        llamada();
                        break;

                }
                if ( menuOptionId != R.id.nav_logout && menuOptionId != R.id.nav_call_center)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();


            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        openFragment(navigationView, 0);
    }

    public void startComponents() {
        fab = findViewById(R.id.fab);
        fab.hide();

        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Intent i;
                switch (current) {
                    case CRECIMIENTO:
                        if (Constants.currentBaby == null) {
                            Toast.makeText(this, "No has agregado algún dependiente", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        i = new Intent(MainActivity.this, DatosActivity.class);
                        startActivity(i);
                        break;
                    case PEDIATRAS:
                        if(Constants.currentBaby == null) {
                            Toast.makeText( this, "Selecciona un dependiente", Toast.LENGTH_SHORT ).show();
                            return;
                        }
                        i = new Intent(MainActivity.this, DatosSintomasActivity.class);
                        startActivity(i);
                        break;
                    case VACUNAS:
                        if(Constants.currentBaby == null) {
                            Toast.makeText( this, "Selecciona un dependiente", Toast.LENGTH_SHORT ).show();
                            return;
                        }
                        i = new Intent(MainActivity.this, VacunaActivity.class);
                        startActivity(i);
                        break;
                }
                //TODO: Implmentar
                //switch() {
                //
                //}
                //onSearchRequested();
                //show();
                /*Snackbar.make( view, "Replace with your own action", Snackbar.LENGTH_LONG )
                        .setAction( "Action", null ).show();*/
                break;
        }
    }

    public void showFab(boolean show) {
        if (show && fab.getVisibility() != View.VISIBLE) {
            fab.show();
        } else if (!show && fab.getVisibility() == View.VISIBLE) {
            fab.hide();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.i("MAIN - Submit", s);
                searchItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.i("MAIN - Text", s);
                return true;
            }
        });
        searchItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void llamada(){
        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:086"));
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
            return;
        startActivity(i);
        finish();
    }

    private void openFragment(NavigationView navigationView, @IdRes int resMenu) {
        if (resMenu == 0) {
            resMenu = R.id.nav_home;
        }
        Log.i("Main", "resid: " + resMenu);
        MenuItem item = navigationView.getMenu().findItem(resMenu);
        navigationView.setCheckedItem(resMenu);
        switch (resMenu) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PrincipalFragment()).commit();
                break;
        }
        //onNavigationItemSelected( item );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_choose_child) {
            mostrarBebe();
        } /*else if(item.getItemId() == R.id.action_vacuna) {
            if(Constants.currentBaby == null) {
                Toast.makeText( this, "Selecciona un dependiente", Toast.LENGTH_SHORT ).show();
            } else {
                Intent intent = new Intent( MainActivity.this, VacunaActivity.class );
                startActivity( intent );
            }
        }*/
        return super.onOptionsItemSelected(item);
    }

    public void mostrarBebe() {

        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        Call<String> resp = service.getListaDependiente();

        resp.enqueue( new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() != 200) {
                    Toast.makeText( getApplicationContext() , "Ocurrió un error\n " + response.code() + " " + response.message(), Toast.LENGTH_LONG ).show();
                    return;
                }
                try {

                    JSONObject jresponse = new JSONObject(response.body());
                    if(!jresponse.getBoolean( "OK" )) {
                        Toast.makeText( getApplicationContext(), jresponse.getString("message"), Toast.LENGTH_LONG ).show();
                        return;
                    }

                    final List<Dependiente> lista = getListaDependientes(jresponse.getJSONObject("data"));


                    if (lista.size() <= 0) return;
                    final String[] babies = new String[lista.size()];
                    int i = 0;
                    for (Dependiente d : lista) {
                        babies[i++] = d.getNombreCompleto();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Elije un bebé")
                            .setItems(babies, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        Constants.currentBaby = lista.get(which).getId();
                                        Constants.fechaNac = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.getDefault()).parse(lista.get(which).getFecNacimiento() + " 00:00:00");
                                        Constants.sexo = lista.get(which).isSexo();
                                        Constants.imagenVacuna = lista.get(which).getImagenVacuna();
                                    } catch (Exception e) {
                                        Log.e("MainActivity", e.getMessage());
                                    }
                                    Toast.makeText(MainActivity.this, babies[which], Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.create().show();

                } catch(JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText( getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG ).show();

            }
        } );
    }

    @Override
    public void showFloatingButton(boolean show) {
        showFab(show);
    }

    /*public static class DependienteAsync extends AsyncTask<Void, Void, List<Dependiente>> {
        private WeakReference<MainActivity> activityReference;

        DependienteAsync(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Dependiente> doInBackground(Void... voids) {
            return AppDatabase.getInstance(activityReference.get()).dependiente().getList();
        }

        @Override
        protected void onPostExecute(final List<Dependiente> list) {
            if (list.size() <= 0) return;
            final String[] babies = new String[list.size()];
            int i = 0;
            for (Dependiente d : list) {
                babies[i++] = d.getNombre();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(activityReference.get());
            builder.setTitle("Elije un bebé")
                    .setItems(babies, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Constants.currentBaby = list.get(which).getId();
                                Constants.fechaNac = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.getDefault()).parse(list.get(which).getFecNacimiento() + " 00:00:00");
                                Constants.sexo = list.get(which).isSexo();
                            } catch (Exception e) {
                                Log.e("MainActivity", e.getMessage());
                            }
                            Toast.makeText(activityReference.get(), babies[which], Toast.LENGTH_LONG).show();
                        }
                    });
            builder.create().show();
        }
    }*/


    private List<Dependiente> getListaDependientes(JSONObject data) throws JSONException {
        JSONArray lista = data.getJSONArray("dependientes");
        List<Dependiente> dependientes = new ArrayList<>();

        JSONObject object;
        for(int i = 0, length = lista.length(); i < length; i++) {
            object = lista.getJSONObject(i);
            Dependiente dependiente = new Dependiente();

            dependiente.setId(object.getInt("id"));
            dependiente.setNombre(object.getString( "nombre" ));
            dependiente.setApellidos(object.getString( "apellidos" ));
            dependiente.setFecNacimiento(object.getString("fecNacimiento"));
            dependiente.setImagenVacuna(object.has("imagenVacuna") ? object.getString("imagenVacuna") : null);

            if (object.getInt("sexo") == 0){
                dependiente.setSexo(false);
            }else {
                dependiente.setSexo(true);
            }

            dependientes.add( dependiente );
        }

        return dependientes;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        menuOptionId = item.getItemId();
        isMenuOptionChecked = item.isChecked();
        drawer.closeDrawer(GravityCompat.START);
        setTitle(item.getTitle());
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void enviarDatos() {
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        Call<String> resp = service.logout();
        progressDialog = UI.showWaitDialog(MainActivity.this);

        resp.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    Toast.makeText(MainActivity.this, "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if (!jresponse.getBoolean("OK")) {
                        Toast.makeText(MainActivity.this, jresponse.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                    progressDialog.dismiss();
                    sharedPreferences.removeToken();

                    //Logout de Facebook
                    LoginManager.getInstance().logOut();

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
                progressDialog.dismiss();
            }
        });
    }
}
