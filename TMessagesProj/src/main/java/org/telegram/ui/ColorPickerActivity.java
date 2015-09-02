package org.telegram.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.telegram.android.AndroidUtilities;
import org.telegram.android.LocaleController;
import org.telegram.android.MessagesController;
import org.telegram.android.NotificationCenter;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.R;
import org.telegram.messenger.RPCRequest;
import org.telegram.messenger.TLObject;
import org.telegram.messenger.TLRPC;
import org.telegram.ui.Adapters.BaseFragmentAdapter;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Color.ColorSelectorDialog;
import org.telegram.ui.Components.LayoutHelper;

/**
 * Created by xxfacusxx on 09/06/15.
 */
public class ColorPickerActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {

    private ListView listView;
    private boolean reseting = false;

    private int val = 0;                        // Color del fondo de la actionbar
    private int valTextActionBar = 0;           // COlor del texto de la actionbar
    private int valBackgroundPerfilDrawer = 0;  // Color del fondo del perfil del Drawer
    private int valNickDrawer = 0;              // Color del nick del drawer
    private int valNumeroDrawer = 0;            // Color del numero del drawer
    private int iconosColor = 0;                // Color de los iconos del drawer
    private int textosColor = 0;                // Color de los textos del drawer

    private int principalSectionRow;            // Ventana Principal - Titulo
    private int actionbarcolor;                 // Color de la actionbar
    private int tituloPrincipalColor;           // Titulo pantalla principal
    private int divisor1;                       // Divisor 1 de seccion
    private int drawerSectionRow;               // Estilo de Cortina - Titulo
    private int seccionAvatarDrawer;            // Color del fondo del perfil del Drawer
    private int nickColorDrawer;                // Color del nick del drawer
    private int numeroColorDrawer;              // Color del numero en el drawer
    private int iconosColorDrawer;              // Color de los iconos del Drawer
    private int textoIconosColorDrawer;         // Color del texto del Drawer
    private int divisor2;                       // Divisor 2 de seccion
    private int chatSectionRow;                 // Ajustes de chat - Titulo
    private int tituloMensajes;                 // Color del titulo de mensajes
    private int estadoMensajes;                 // Color del estado de mensajes
    private int botonEnviar;                    // Color del boton enviar
    private int divisor3;                       // Divisor 3 de seccion
    private int settingsSectionRow;             // Ajustes generales - Titulo
    private int fondoAvatarSettings;            // Color de fondo avatar settings
    private int nickSettings;                   // Color del nick en settings
    private int estadoSettings;                 // Color del estado en settings
    private int titulosSettings;                // Color de los titulos de seccion de settings
    private int textosSettings;                 // Color de los titulos de settings
    private int textSubSettingsColor;           // Color de los subtitulos de settings
    private int divisor4;                       // Divisor 4 de seccion
    private int resetSectionRow;                // Titulo de resetear
    private int resetTematizationRow;           // Resetear tematizacion
    private int rowCount = 0;


    @Override
    public boolean onFragmentCreate() {

        rowCount = 0;
        principalSectionRow = rowCount++;
        actionbarcolor = rowCount++;
        tituloPrincipalColor = rowCount++;
        divisor1 = rowCount++;
        drawerSectionRow = rowCount++;
        seccionAvatarDrawer = rowCount++;
        nickColorDrawer = rowCount++;
        numeroColorDrawer = rowCount++;
        iconosColorDrawer = rowCount++;
        textoIconosColorDrawer = rowCount++;
        divisor2 = rowCount++;
        chatSectionRow = rowCount++;
        tituloMensajes = rowCount++;
        estadoMensajes = rowCount++;
        botonEnviar = rowCount++;
        divisor3 = rowCount++;
        settingsSectionRow = rowCount++;
        fondoAvatarSettings = rowCount++;
        nickSettings = rowCount++;
        estadoSettings = rowCount++;
        titulosSettings = rowCount++;
        textosSettings = rowCount++;
        textSubSettingsColor = rowCount++;
        divisor4 = rowCount++;
        resetSectionRow = rowCount++;
        resetTematizationRow = rowCount++;

        NotificationCenter.getInstance().addObserver(this, NotificationCenter.colorPickerUpdated);

        return super.onFragmentCreate();
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.colorPickerUpdated);
    }

    @Override
    public View createView(final Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString("Personalization", R.string.Personalization));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        fragmentView = new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout) fragmentView;

        listView = new ListView(context);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView(listView);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.width = LayoutHelper.MATCH_PARENT;
        layoutParams.height = LayoutHelper.MATCH_PARENT;
        listView.setLayoutParams(layoutParams);
        listView.setAdapter(new ListAdapter(context));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int i, long l) {

                boolean enabled = false;
                if (i == resetTematizationRow) {
                    if (reseting) {
                        return;
                    }
                    reseting = true;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getParentActivity());
                    alertDialogBuilder.setTitle(LocaleController.getString("Atencion", R.string.Atencion));
                    alertDialogBuilder
                            .setMessage(LocaleController.getString("MensajeBorrarColor", R.string.MensajeBorrarColor))
                            .setCancelable(false)
                            .setPositiveButton(LocaleController.getString("Yes", R.string.Yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    TLRPC.TL_account_resetNotifySettings req = new TLRPC.TL_account_resetNotifySettings();
                                    ConnectionsManager.getInstance().performRpc(req, new RPCRequest.RPCRequestDelegate() {
                                        @Override
                                        public void run(TLObject response, TLRPC.TL_error error) {
                                            AndroidUtilities.runOnUIThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    MessagesController.getInstance().enableJoined = true;
                                                    reseting = false;
                                                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("Tematizacion", Activity.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.clear();
                                                    editor.commit();
                                                    if (listView != null) {
                                                        listView.invalidateViews();
                                                    }
                                                    if (getParentActivity() != null) {
                                                        Toast toast = Toast.makeText(getParentActivity(), LocaleController.getString("ResetPersonalizationText", R.string.ResetPersonalizationText), Toast.LENGTH_SHORT);
                                                        toast.show();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(LocaleController.getString("No", R.string.No), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    reseting = false;

                } else if (i == actionbarcolor || i == tituloPrincipalColor || i == titulosSettings || i == textosSettings ||
                        i == seccionAvatarDrawer || i == nickColorDrawer || i == numeroColorDrawer || i == textSubSettingsColor ||
                        i == tituloMensajes || i == estadoMensajes || i == fondoAvatarSettings || i == nickSettings ||
                        i == estadoSettings || i == botonEnviar || i == iconosColorDrawer || i == textoIconosColorDrawer) {
                    if (getParentActivity() == null) {
                        return;
                    }

                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("Tematizacion", Activity.MODE_PRIVATE);
                    final SharedPreferences preferences1 = ApplicationLoader.applicationContext.getSharedPreferences("Tematizacion", Activity.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = preferences1.edit();
                    if (i == actionbarcolor) {
                        val = preferences.getInt("ActionBarColor", 0xff0c6a8c);
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("ActionBarColor", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("ActionBarColor", 0xff0c6a8c));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == tituloPrincipalColor) {
                        valTextActionBar = preferences.getInt("TituloPrincipalColor", 0xffffffff);
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("TituloPrincipalColor", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("TituloPrincipalColor", 0xffffffff));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == seccionAvatarDrawer) {
                        valBackgroundPerfilDrawer = preferences.getInt("SeccionAvatarDrawer", 0xff0c6a8c);
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("SeccionAvatarDrawer", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("SeccionAvatarDrawer", 0xff0c6a8c));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == nickColorDrawer) {
                        valNickDrawer = preferences.getInt("NickColorDrawer", 0xffffffff);
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("NickColorDrawer", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("NickColorDrawer", 0xffffffff));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == numeroColorDrawer) {
                        valNumeroDrawer = preferences.getInt("NumeroColorDrawer", 0xffb3e5f7);
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("NumeroColorDrawer", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("NumeroColorDrawer", 0xffb3e5f7));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == iconosColorDrawer) {
                        iconosColor = preferences.getInt("IconosColorDrawer", 0xff0d5e80);
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("IconosColorDrawer", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("IconosColorDrawer", 0xff0d5e80));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == textoIconosColorDrawer) {
                        textosColor = preferences.getInt("TextoIconosColorDrawer", 0xff424242);
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("TextoIconosColorDrawer", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("TextoIconosColorDrawer", 0xff424242));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == titulosSettings) {
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("TitulosSettings", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("TitulosSettings", 0xff0c6a8c));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == textosSettings) {
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("TextosSettings", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("TextosSettings", 0xff424242));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == textSubSettingsColor) {
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("TextSubSettingsColor", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("TextSubSettingsColor", 0xff777777));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == tituloMensajes) {
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("TituloMensajesColor", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("TituloMensajesColor", 0xffffffff));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == estadoMensajes) {
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("EstadoMensajesColor", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("EstadoMensajesColor", 0xffb3e5f7));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == fondoAvatarSettings) {
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("FondoAvatarSettingsColor", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("FondoAvatarSettingsColor", 0xff0c6a8c));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == nickSettings) {
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("NickSettingsColor", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("NickSettingsColor", 0xffffffff));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == estadoSettings) {
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("EstadoSettingsColor", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("EstadoSettingsColor", 0xffb3e5f7));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    } else if (i == botonEnviar) {
                        final ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getParentActivity(),
                                new ColorSelectorDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        editor.putInt("BotonEnviarColor", color);
                                        editor.commit();
                                        listView.invalidateViews();
                                    }
                                },
                                preferences.getInt("BotonEnviarColor", 0xff0c6a8c));
                        colorSelectorDialog.setTitle(LocaleController.getString("ElegirColor", R.string.ElegirColor));
                        colorSelectorDialog.show();
                    }
                }
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(!enabled);
                }
            }
        });

        return fragmentView;
    }

    @Override

    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationCenter.colorPickerUpdated) {
            listView.invalidateViews();
        }
    }

    public int getValActionBar() {
        return val;
    }

    public int getValTextActionBar() {
        return valTextActionBar;
    }

    public int getIconColorDrawer() {
        int valor = 0;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("Tematizacion", Activity.MODE_PRIVATE);
        if (preferences.getInt("IconosColorDrawer", 0xff0d5e80) != iconosColor) {
            valor = preferences.getInt("IconosColorDrawer", 0xff0d5e80);
        }
        return valor;
    }

    private class ListAdapter extends BaseFragmentAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int i) {
            return !(i == principalSectionRow || i == drawerSectionRow || i == settingsSectionRow ||
                    i == resetSectionRow || i == chatSectionRow);
        }

        @Override
        public int getCount() {
            return rowCount;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            int type = getItemViewType(i);
            if (type == 0) {
                if (view == null) {
                    view = new HeaderCell(mContext);
                }
                if (i == principalSectionRow) {
                    ((HeaderCell) view).setText(LocaleController.getString("VentanaPrincipal", R.string.VentanaPrincipal));
                } else if (i == chatSectionRow) {
                    ((HeaderCell) view).setText(LocaleController.getString("ChatSection", R.string.ChatSection));
                } else if (i == drawerSectionRow) {
                    ((HeaderCell) view).setText(LocaleController.getString("DrawerSection", R.string.DrawerSection));
                } else if (i == settingsSectionRow) {
                    ((HeaderCell) view).setText(LocaleController.getString("SettingsSection", R.string.SettingsSections));
                } else if (i == resetSectionRow) {
                    ((HeaderCell) view).setText(LocaleController.getString("Reset", R.string.Reset));
                }
            } else if (type == 1) {
                if (view == null) {
                    view = new TextDetailSettingsCell(mContext);
                }

                TextDetailSettingsCell textCell = (TextDetailSettingsCell) view;

                if (i == resetTematizationRow) {
                    textCell.setMultilineDetail(true);
                    textCell.setTextAndValue(LocaleController.getString("ResetAllPersonalizations", R.string.ResetAllPersonalizations), LocaleController.getString("UndoAllCustomColors", R.string.UndoAllCustomColors), false);
                }
            } else if (type == 2) {
                if (view == null) {
                    view = new TextColorCell(mContext);
                }

                final TextColorCell textCell = (TextColorCell) view;
                final SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("Tematizacion", Activity.MODE_PRIVATE);

                if (i == actionbarcolor) {
                    textCell.setTextAndColor(LocaleController.getString("ActBarColor", R.string.ActBarColor), preferences.getInt("ActionBarColor", 0xff0c6a8c), true);
                    onResume();
                } else if (i == tituloPrincipalColor) {
                    textCell.setTextAndColor(LocaleController.getString("TituloPantallaPrincipal", R.string.TituloPantallaPrincipal), preferences.getInt("TituloPrincipalColor", 0xffffffff), true);
                    onResume();
                } else if (i == seccionAvatarDrawer) {
                    textCell.setTextAndColor(LocaleController.getString("SecAvatarDrawer", R.string.SecAvatarDrawer), preferences.getInt("SeccionAvatarDrawer", 0xff0c6a8c), true);
                } else if (i == nickColorDrawer) {
                    textCell.setTextAndColor(LocaleController.getString("NickDrawerColor", R.string.NickDrawerColor), preferences.getInt("NickColorDrawer", 0xffffffff), true);
                } else if (i == numeroColorDrawer) {
                    textCell.setTextAndColor(LocaleController.getString("NumeroDrawerColor", R.string.NumeroDrawerColor), preferences.getInt("NumeroColorDrawer", 0xffb3e5f7), true);
                } else if (i == iconosColorDrawer) {
                    textCell.setTextAndColor(LocaleController.getString("IconosDrawerColor", R.string.IconosDrawerColor), preferences.getInt("IconosColorDrawer", 0xff0d5e80), true);
                } else if (i == textoIconosColorDrawer) {
                    textCell.setTextAndColor(LocaleController.getString("TextoIconosDrawerColor", R.string.TextoIconosDrawerColor), preferences.getInt("TextoIconosColorDrawer", 0xff424242), true);
                } else if (i == titulosSettings) {
                    textCell.setTextAndColor(LocaleController.getString("TitSettingsColor", R.string.TitSettingsColor), preferences.getInt("TitulosSettings", 0xff0c6a8c), true);
                } else if (i == textosSettings) {
                    textCell.setTextAndColor(LocaleController.getString("TextSettingsColor", R.string.TextSettingsColor), preferences.getInt("TextosSettings", 0xff424242), true);
                } else if (i == textSubSettingsColor) {
                    textCell.setTextAndColor(LocaleController.getString("TextSubSettingsColor", R.string.TextSubSettingsColor), preferences.getInt("TextSubSettingsColor", 0xff777777), true);
                } else if (i == tituloMensajes) {
                    textCell.setTextAndColor(LocaleController.getString("TitulosMensajesColor", R.string.TituloMensajesColor), preferences.getInt("TituloMensajesColor", 0xffffffff), true);
                } else if (i == estadoMensajes) {
                    textCell.setTextAndColor(LocaleController.getString("EstadoMensajesColor", R.string.EstadoMensajesColor), preferences.getInt("EstadoMensajesColor", 0xffb3e5f7), true);
                } else if (i == fondoAvatarSettings) {
                    textCell.setTextAndColor(LocaleController.getString("FondoAvatarSettingsColor", R.string.FondoAvatarSettingsColor), preferences.getInt("FondoAvatarSettingsColor", 0xff0c6a8c), true);
                } else if (i == nickSettings) {
                    textCell.setTextAndColor(LocaleController.getString("NickSettingsColor", R.string.NickSettingsColor), preferences.getInt("NickSettingsColor", 0xffffffff), true);
                } else if (i == estadoSettings) {
                    textCell.setTextAndColor(LocaleController.getString("EstadoSettingsColor", R.string.EstadoSettingsColor), preferences.getInt("EstadoSettingsColor", 0xffb3e5f7), true);
                } else if (i == botonEnviar) {
                    textCell.setTextAndColor(LocaleController.getString("BotonEnviarColor", R.string.BotonEnviarColor), preferences.getInt("BotonEnviarColor", 0xff0c6a8c), true);
                }

            } else if (type == 3) {
                if (view == null) {
                    view = new ShadowSectionCell(mContext);
                }
            }

            return view;
        }

        @Override
        public int getItemViewType(int i) {
            if (i == principalSectionRow || i == drawerSectionRow || i == settingsSectionRow ||
                    i == resetSectionRow || i == chatSectionRow) {
                return 0;
            } else if (i == divisor1 || i == divisor2 || i == divisor3 || i == divisor4) {
                return 3;
            } else if (i == actionbarcolor || i == tituloPrincipalColor || i == titulosSettings ||
                    i == textosSettings || i == seccionAvatarDrawer || i == nickColorDrawer ||
                    i == numeroColorDrawer || i == textSubSettingsColor || i == tituloMensajes ||
                    i == estadoMensajes || i == fondoAvatarSettings || i == nickSettings ||
                    i == estadoSettings || i == botonEnviar || i == iconosColorDrawer ||
                    i == textoIconosColorDrawer) {
                return 2;
            } else {
                return 1;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("Tematizacion", Activity.MODE_PRIVATE);
        if (actionBar != null && (preferences.getInt("ActionBarColor", 0xff0c6a8c) != val || preferences.getInt("TituloPrincipalColor", 0xffffffff) != valTextActionBar)) {
            if (preferences.getInt("ActionBarColor", 0xff0c6a8c) != val && preferences.getInt("TituloPrincipalColor", 0xffffffff) != valTextActionBar) {
                actionBar.setBackgroundColor(preferences.getInt("ActionBarColor", 0xff0c6a8c));
                actionBar.setColorTexto(preferences.getInt("TituloPrincipalColor", 0xffffffff));
            } else if (preferences.getInt("ActionBarColor", 0xff212121) != val){
                actionBar.setBackgroundColor(preferences.getInt("ActionBarColor", 0xff0c6a8c));
            } else if (preferences.getInt("TituloPrincipalColor", 0xffffffff) != valTextActionBar) {
                actionBar.setColorTexto(preferences.getInt("TituloPrincipalColor", 0xffffffff));
            }
        }
    }


}
