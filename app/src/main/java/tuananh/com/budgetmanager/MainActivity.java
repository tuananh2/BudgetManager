package tuananh.com.budgetmanager;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    public static final int LAYOUT_MAIN_MENU           =0;
    public static final int LAYOUT_ADD_TRACSACTION     =1;

    public static final int MENU_HISTORY                =0;
    public static final int MENU_STATISTIC              =1;

    public static final int MENU_DAILY_HISTORY          =0;
    public static final int MENU_WEEKLY_HISTORY         =1;
    public static final int MENU_MONTHLY_HISTORY        =2;
    public static final String TAG                      ="Anhlt2";
    public List<String> listCategories;


    public Button btnAddTransaction ;
    public int m_nextLayout, m_currentLayout;//main layout or add transition layout
    public int m_nextMenuLayout, m_currentMenuLayout;//statistic menu or history menu
    public int m_nextHistoryMenuLayout, m_currentHistoryMenuLayout;//daily, weekly, or monthly
    public int m_selectedMenu;
    public Database myDatabase = new Database(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main_layout);

      //  m_selectedMenu =MENU_HISTORY;

        m_nextLayout = LAYOUT_MAIN_MENU;
        m_nextMenuLayout = MENU_HISTORY;
        m_nextHistoryMenuLayout= MENU_DAILY_HISTORY;

        goToLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();

        myDatabase.removeData(Database.Entries.TABLE_CATEGORIES);
        myDatabase.addCategory("food","expense");
        myDatabase.addCategory("salary","income");
        myDatabase.addCategory("entertainment","expense");
        myDatabase.addCategory("gas","expense");
        myDatabase.updateData("income", Database.Entries.TABLE_CATEGORIES);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(KeyEvent.KEYCODE_BACK ==keyCode)
        {
            if(LAYOUT_ADD_TRACSACTION == m_currentLayout) {
                m_nextLayout = LAYOUT_MAIN_MENU;
                m_nextMenuLayout = MENU_HISTORY;
                m_nextHistoryMenuLayout = MENU_DAILY_HISTORY;
                goToLayout();
                return false;
            }
        }
        return super.onKeyDown(keyCode,event);
    }

    public void goToLayout()
    {
        switch(m_nextLayout)
        {
            case LAYOUT_ADD_TRACSACTION:
            {
                setContentView(R.layout.add_transaction_layout);

                EditText transactionNote = (EditText) findViewById(R.id.transaction_edit_note);
                LinearLayout transactionCategoryName = (LinearLayout) findViewById(R.id
                        .transaction_category_name);

                transactionNote.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if(KeyEvent.KEYCODE_ENTER==i)
                        {
                            if(LAYOUT_ADD_TRACSACTION == m_currentLayout)
                            {
                                m_nextLayout = LAYOUT_MAIN_MENU;
                                m_nextMenuLayout = MENU_HISTORY;
                                m_nextHistoryMenuLayout = MENU_DAILY_HISTORY;
                                goToLayout();
                            }
                        }
                        return false;
                    }
                });

                m_currentLayout = LAYOUT_ADD_TRACSACTION;


                transactionCategoryName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor cursor = myDatabase.readDatabase(Database.Entries.TABLE_CATEGORIES);
                        LinearLayout listCategories = (LinearLayout) findViewById(R.id
                                .list_categories);

                        //listCategories.setVisibility(View.INVISIBLE);
                        if(cursor.moveToFirst())
                        {
                            do {
                                String category = cursor.getString(cursor.getColumnIndex(Database
                                        .Entries.NAME));
                                final TextView categoryCard = new TextView(getApplicationContext());
                                categoryCard.setText(category);
                                categoryCard.setTextSize(TypedValue.COMPLEX_UNIT_PT,11);
                                categoryCard.setBackgroundResource(R.drawable.selectable_category_card_background);
                                categoryCard.setPadding(10,6,0,6);
                                categoryCard.setTextColor(Color.BLACK);
                                listCategories.addView(categoryCard);

                                cursor.moveToNext();

                                categoryCard.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        TextView categoryName = (TextView) findViewById(R.id
                                                .transaction_edit_category_name);
                                        categoryName.setText(categoryCard.getText());
                                    }
                                });
                            }while(!cursor.isAfterLast());
                        }
                    }
                });

//                transactionCategoryName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                    @Override
//                    public void onFocusChange(View view, boolean hasFocus) {
//                        ScrollView listCategories = (ScrollView) findViewById(R.id
//                                .list_categories);
//                        if(hasFocus)
//                        {
//                            listCategories.setVisibility(View.VISIBLE);
//                        }
//                        else{
//                            listCategories.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                });

                break;
            }
            case LAYOUT_MAIN_MENU:
            {
                setContentView(R.layout.main_layout);
                m_currentLayout = LAYOUT_MAIN_MENU;
                btnAddTransaction = (Button) findViewById(R.id.add_transaction_button);
                btnAddTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "onClick: ");
                        m_nextLayout  = LAYOUT_ADD_TRACSACTION;
                        goToLayout();
                    }
                });
                switch (m_nextMenuLayout)
                {
                    case MENU_HISTORY:
                    {
                        //add top bar menu
                        RelativeLayout contentMenu = (RelativeLayout) findViewById(R.id.menu_content_layout);
                        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
                        View historyMenuTopBar = inflater.inflate(R.layout.history_menu_bar,
                                (ViewGroup) findViewById(R.id.history_root_menu));
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        historyMenuTopBar.setLayoutParams(layoutParams);
                        contentMenu.addView(historyMenuTopBar);

                        switch (m_nextHistoryMenuLayout)
                        {
                            case MENU_DAILY_HISTORY:
                            {

                            }
                            case MENU_WEEKLY_HISTORY:
                            {

                            }
                            case MENU_MONTHLY_HISTORY:
                            {

                            }
                            default:
                                break;
                        }
                    }
                    case MENU_STATISTIC:
                    {

                    }
                    default:
                        break;
                }
            }
            default:
                break;
        }
    }

}
