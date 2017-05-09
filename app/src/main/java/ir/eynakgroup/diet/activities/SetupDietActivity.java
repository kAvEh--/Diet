package ir.eynakgroup.diet.activities;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
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

    private class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> implements View.OnClickListener{

        private List<Chat> chatList;
        private Context context;

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

                holder.allergyLayout.findViewById(R.id.layout_egg).setOnClickListener(this);
                holder.allergyLayout.findViewById(R.id.layout_eggplant).setOnClickListener(this);
                holder.allergyLayout.findViewById(R.id.layout_fava).setOnClickListener(this);
                holder.allergyLayout.findViewById(R.id.layout_peanut).setOnClickListener(this);
                holder.allergyLayout.findViewById(R.id.layout_shrimp).setOnClickListener(this);
                holder.allergyLayout.findViewById(R.id.layout_soya).setOnClickListener(this);
                holder.allergyLayout.findViewById(R.id.layout_walnut).setOnClickListener(this);
                holder.allergyLayout.findViewById(R.id.layout_zucchini).setOnClickListener(this);

            }
            holder.chatLayout.setLayoutParams(params);
        }

        @Override
        public int getItemCount() {
            return chatList.size();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.layout_egg:
                    if(!v.getTag().equals("checked")){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_egg)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        v.setTag("checked");
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_egg)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        v.setTag("unchecked");
                    }
                    break;
                case R.id.layout_eggplant:
                    if(!v.getTag().equals("checked")){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_eggplant)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        v.setTag("checked");
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_eggplant)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        v.setTag("unchecked");
                    }
                    break;
                case R.id.layout_peanut:
                    if(!v.getTag().equals("checked")){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_peanut)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        v.setTag("checked");
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_peanut)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        v.setTag("unchecked");
                    }
                    break;
                case R.id.layout_fava:
                    if(!v.getTag().equals("checked")){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_fava)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        v.setTag("checked");
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_fava)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        v.setTag("unchecked");
                    }
                    break;
                case R.id.layout_shrimp:
                    if(!v.getTag().equals("checked")){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_shrimp)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        v.setTag("checked");
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_shrimp)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        v.setTag("unchecked");
                    }
                    break;
                case R.id.layout_soya:
                    if(!v.getTag().equals("checked")){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_soya)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        v.setTag("checked");
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_soya)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        v.setTag("unchecked");
                    }
                    break;
                case R.id.layout_walnut:
                    if(!v.getTag().equals("checked")){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_walnut)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        v.setTag("checked");
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_walnut)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        v.setTag("unchecked");
                    }
                    break;
                case R.id.layout_zucchini:
                    if(!v.getTag().equals("checked")){
                        v.setBackgroundResource(R.drawable.background_allergy_solid);
                        ((TextView)v.findViewById(R.id.txt_zucchini)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        v.setTag("checked");
                    }else{
                        v.setBackgroundResource(R.drawable.background_radio_button);
                        ((TextView)v.findViewById(R.id.txt_zucchini)).setTextColor(ContextCompat.getColor(context, R.color.colorEditText));
                        v.setTag("unchecked");
                    }
                    break;
                default:

            }
        }
    }


}
