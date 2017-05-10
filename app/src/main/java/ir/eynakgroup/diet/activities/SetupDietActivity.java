package ir.eynakgroup.diet.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.utils.view.CustomTextView;

/**
 * Created by Shayan on 5/6/2017.
 */

public class SetupDietActivity extends BaseActivity implements View.OnClickListener {

    private List<Chat> chatList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChatAdapter mAdapter;
    private FlexboxLayout flexBox;
    private ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        imageBack = (ImageView) findViewById(R.id.img_back);
        final View backParent = (View) imageBack.getParent();  // button: the view you want to enlarge hit area
        backParent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                imageBack.getHitRect(rect);
                rect.top -= 50;    // increase top hit area
                rect.left -= 50;   // increase left hit area
                rect.bottom += 50; // increase bottom hit area
                rect.right += 50;  // increase right hit area
                backParent.setTouchDelegate(new TouchDelegate(rect, imageBack));
            }
        });

        imageBack.setOnClickListener(this);
        flexBox = (FlexboxLayout) findViewById(R.id.flexbox_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ChatAdapter(this, chatList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.activity_orientation_margin_2x)));
        recyclerView.setAdapter(mAdapter);

        prepareChatData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showExitDialog();
    }

    private void showExitDialog(){
        new AlertDialog.Builder(this)
                .setMessage(R.string.exit_alert)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.exit_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    private class Chat {
        private String chatText;
        private Type chatType;

        public Chat(String chatText, Type chatType) {
            setChatText(chatText);
            setChatType(chatType);
        }

        public Chat(Type chatType) {
            setChatType(chatType);
        }

        public Type getChatType() {
            return chatType;
        }

        public void setChatType(Type chatType) {
            this.chatType = chatType;
        }

        public String getChatText() {
            return chatText;
        }

        public void setChatText(String chatText) {
            this.chatText = chatText;
        }
    }

    private enum Type {
        SELF_APP, USER, ALLERGY
    }

    private void prepareChatData() {
        chatList.add(new Chat("سلام شایان، خوشحالم که تصمیم گرفتی رژیمت رو شروع کنی.", Type.SELF_APP));
        chatList.add(new Chat("سلام", Type.USER));
        chatList.add(new Chat("سلام شایان، خوشحالم که تصمیم گرفتی رژیمت رو شروع کنی.", Type.SELF_APP));
        chatList.add(new Chat("سلام", Type.USER));
        chatList.add(new Chat("سلام شایان، خوشحالم که تصمیم گرفتی رژیمت رو شروع کنی.", Type.SELF_APP));
        chatList.add(new Chat("سلام", Type.USER));
        chatList.add(new Chat("سلام شایان، خوشحالم که تصمیم گرفتی رژیمت رو شروع کنی.", Type.SELF_APP));
        chatList.add(new Chat("سلام", Type.USER));
        chatList.add(new Chat("سلام شایان، خوشحالم که تصمیم گرفتی رژیمت رو شروع کنی.", Type.SELF_APP));
        chatList.add(new Chat("سلام", Type.USER));
        chatList.add(new Chat("سلام شایان، خوشحالم که تصمیم گرفتی رژیمت رو شروع کنی.", Type.SELF_APP));
        chatList.add(new Chat("سلام", Type.USER));
        chatList.add(new Chat("سلام شایان، خوشحالم که تصمیم گرفتی رژیمت رو شروع کنی.", Type.SELF_APP));
        chatList.add(new Chat("سلام", Type.USER));
        chatList.add(new Chat("از طریق لیست زیر غذاهایی را که به آن‌ها حساسیت دارید، انتخاب کنید.", Type.SELF_APP));
        chatList.add(new Chat(Type.ALLERGY));


        addResponseView("باشه");
        addResponseView("حمید پسر خوبی است.");
        addResponseView("باشه");
        addResponseView("حمید پسر خوبی است.");

        mAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void addListItem(String chatText, Type chatType){
        chatList.add(new Chat(chatText, chatType));
        mAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void addResponseView(String response) {
        final CustomTextView textView = new CustomTextView(this);
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getResources().getDimensionPixelSize(R.dimen.activity_orientation_margin_2x), 0, 0, getResources().getDimensionPixelSize(R.dimen.activity_orientation_margin));
        textView.setLayoutParams(params);
        int padding = getResources().getDimensionPixelSize(R.dimen.activity_orientation_margin);
        textView.setPadding(padding, padding, padding, padding);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        textView.setText(response);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        textView.setBackgroundResource(R.drawable.background_text_solid);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                addListItem(tv.getText().toString().trim(), Type.USER);
            }
        });

        flexBox.addView(textView);
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }

    private class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> implements View.OnClickListener {

        private List<Chat> chatList;
        private Context context;
        private boolean[] checked = new boolean[8];

        public class MyViewHolder extends RecyclerView.ViewHolder {
            CustomTextView chatText;
            RelativeLayout chatLayout;
            LinearLayout allergyLayout;


            public MyViewHolder(View view) {
                super(view);
                chatLayout = (RelativeLayout) view;
                chatText = (CustomTextView) view.findViewById(R.id.text_chat);
                allergyLayout = (LinearLayout) view.findViewById(R.id.layout_allergy);
            }

        }

        public ChatAdapter(Context context, List<Chat> chatList) {
            this.chatList = chatList;
            this.context = context;
            init();
        }

        private void init(){
            for(int i = 0; i < checked.length; i++)
                checked[i] = false;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item, parent, false);

            return new MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            if (chatList.get(position).getChatType().equals(Type.SELF_APP)) {
                holder.chatText.setVisibility(View.VISIBLE);
                holder.chatText.setBackgroundResource(R.drawable.background_text_stroke);
                holder.chatText.setTextColor(ContextCompat.getColor(context, R.color.colorDescription));
                params.setMargins(0, 0, context.getResources().getDimensionPixelSize(R.dimen.indicator_padding), 0);
                holder.chatText.setText(chatList.get(position).getChatText());
                holder.allergyLayout.setVisibility(View.GONE);

            } else if (chatList.get(position).getChatType().equals(Type.USER)){
                holder.chatText.setVisibility(View.VISIBLE);
                holder.chatText.setBackgroundResource(R.drawable.background_text_solid);
                holder.chatText.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                params.setMargins(context.getResources().getDimensionPixelSize(R.dimen.indicator_padding), 0, 0, 0);
                holder.chatText.setText(chatList.get(position).getChatText());
                holder.allergyLayout.setVisibility(View.GONE);

            } else {
                params.setMargins(0, 0, 0, 0);
                holder.chatText.setVisibility(View.GONE);
                holder.allergyLayout.setVisibility(View.VISIBLE);

                if(checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_egg).getTag().toString())]){
                    holder.allergyLayout.findViewById(R.id.layout_egg).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_egg).findViewById(R.id.txt_egg)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                }else{
                    holder.allergyLayout.findViewById(R.id.layout_egg).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_egg).findViewById(R.id.txt_egg)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                if(checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_eggplant).getTag().toString())]){
                    holder.allergyLayout.findViewById(R.id.layout_eggplant).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_eggplant).findViewById(R.id.txt_eggplant)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                }else{
                    holder.allergyLayout.findViewById(R.id.layout_eggplant).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_eggplant).findViewById(R.id.txt_eggplant)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                if(checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_zucchini).getTag().toString())]){
                    holder.allergyLayout.findViewById(R.id.layout_zucchini).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_zucchini).findViewById(R.id.txt_zucchini)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                }else{
                    holder.allergyLayout.findViewById(R.id.layout_zucchini).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_zucchini).findViewById(R.id.txt_zucchini)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                if(checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_walnut).getTag().toString())]){
                    holder.allergyLayout.findViewById(R.id.layout_walnut).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_walnut).findViewById(R.id.txt_walnut)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                }else{
                    holder.allergyLayout.findViewById(R.id.layout_walnut).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_walnut).findViewById(R.id.txt_walnut)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                if(checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_fava).getTag().toString())]){
                    holder.allergyLayout.findViewById(R.id.layout_fava).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_fava).findViewById(R.id.txt_fava)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                }else{
                    holder.allergyLayout.findViewById(R.id.layout_fava).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_fava).findViewById(R.id.txt_fava)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                if(checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_peanut).getTag().toString())]){
                    holder.allergyLayout.findViewById(R.id.layout_peanut).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_peanut).findViewById(R.id.txt_peanut)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                }else{
                    holder.allergyLayout.findViewById(R.id.layout_peanut).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_peanut).findViewById(R.id.txt_peanut)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                if(checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_shrimp).getTag().toString())]){
                    holder.allergyLayout.findViewById(R.id.layout_shrimp).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_shrimp).findViewById(R.id.txt_shrimp)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                }else{
                    holder.allergyLayout.findViewById(R.id.layout_shrimp).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_shrimp).findViewById(R.id.txt_shrimp)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                if(checked[Integer.valueOf(holder.allergyLayout.findViewById(R.id.layout_soya).getTag().toString())]){
                    holder.allergyLayout.findViewById(R.id.layout_soya).setBackgroundResource(R.drawable.background_allergy_solid);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_soya).findViewById(R.id.txt_soya)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                }else{
                    holder.allergyLayout.findViewById(R.id.layout_soya).setBackgroundResource(R.drawable.background_radio_button);
                    ((TextView)holder.allergyLayout.findViewById(R.id.layout_soya).findViewById(R.id.txt_soya)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                }

                setClickListener(holder);
            }
            holder.chatLayout.setLayoutParams(params);
        }

        @Override
        public int getItemCount() {
            return chatList.size();
        }

        private void setClickListener(MyViewHolder holder){
            holder.allergyLayout.findViewById(R.id.layout_egg).setOnClickListener(this);
            holder.allergyLayout.findViewById(R.id.layout_eggplant).setOnClickListener(this);
            holder.allergyLayout.findViewById(R.id.layout_fava).setOnClickListener(this);
            holder.allergyLayout.findViewById(R.id.layout_peanut).setOnClickListener(this);
            holder.allergyLayout.findViewById(R.id.layout_shrimp).setOnClickListener(this);
            holder.allergyLayout.findViewById(R.id.layout_soya).setOnClickListener(this);
            holder.allergyLayout.findViewById(R.id.layout_walnut).setOnClickListener(this);
            holder.allergyLayout.findViewById(R.id.layout_zucchini).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.layout_egg:
                    if(!checked[Integer.valueOf(v.getTag().toString())]){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_egg)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_egg)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                case R.id.layout_eggplant:
                    if(!checked[Integer.valueOf(v.getTag().toString())]){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_eggplant)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_eggplant)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                case R.id.layout_peanut:
                    if(!checked[Integer.valueOf(v.getTag().toString())]){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_peanut)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_peanut)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                case R.id.layout_fava:
                    if(!checked[Integer.valueOf(v.getTag().toString())]){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_fava)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_fava)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                case R.id.layout_shrimp:
                    if(!checked[Integer.valueOf(v.getTag().toString())]){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_shrimp)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_shrimp)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                case R.id.layout_soya:
                    if(!checked[Integer.valueOf(v.getTag().toString())]){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_soya)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_soya)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                case R.id.layout_walnut:
                    if(!checked[Integer.valueOf(v.getTag().toString())]){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_walnut)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_walnut)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                case R.id.layout_zucchini:
                    if(!checked[Integer.valueOf(v.getTag().toString())]){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_zucchini)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        checked[Integer.valueOf(v.getTag().toString())] = true;
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_zucchini)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        checked[Integer.valueOf(v.getTag().toString())] = false;
                    }
                    break;
                default:

            }
        }

    }


}
