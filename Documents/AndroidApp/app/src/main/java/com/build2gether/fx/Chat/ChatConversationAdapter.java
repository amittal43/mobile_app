package com.build2gether.fx.Chat;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.build2gether.fx.R;
import com.facebook.Profile;

import java.util.ArrayList;

import static com.build2gether.fx.R.drawable.rounded_btn;

/**
 * Created by abhilashnair on 4/6/16.
 */
public class ChatConversationAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private static ArrayList<String> messageList;

    public ChatConversationAdapter(Activity context, ArrayList<String> messageList) {
        super(context, R.layout.content_chat_conversation, messageList);
        this.context = context;
        this.messageList = messageList;

    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.content_chat_conversation, null, true);

        TextView message = (TextView) rowView.findViewById(R.id.Messages);

//        TextView UserMessage = (TextView) rowView.findViewById(R.id.UserMessage);
//        TextView ContactMessage = (TextView) rowView.findViewById(R.id.ContactMessage);

        String[] SenderAndMessage = messageList.get(position).split(":");

        String Sender = SenderAndMessage[0];

        String Message = SenderAndMessage[1];

        String UserId = Profile.getCurrentProfile().getId();

        if (Sender.equals(UserId)) {//Sender is Yourself
            message.setTextColor(Color.BLUE);
//            message.setGravity(Gravity.RIGHT);
            message.setText(Message);
//            message.append(Message);

        } else {//Sender is Contact
            message.setTextColor(Color.RED);
//            message.setGravity(Gravity.RIGHT);
            message.setText(Message);
//            message.append(Message);
        }
        return rowView;
    }*/
        ViewHolder holder;

        String[] SenderAndMessage = messageList.get(position).split(":");

        String Sender = SenderAndMessage[0];

        String Message = SenderAndMessage[1];

        String UserId = Profile.getCurrentProfile().getId();

//        ChatMessage chatMessage = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.list_item_chat_message, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(Sender.equals(UserId)){
            System.out.println("User ID: " + UserId);
            setAlignment(holder, true);
        } else {
            setAlignment(holder, false);
        }
//        boolean myMsg = chatMessage.getIsme() ;//Just a dummy check
        //to simulate whether it me or other sender
//        setAlignment(holder, myMsg);
        holder.txtMessage.setText(Message);
//        messageList.add(Message);

        return convertView;
    }

//    public void add(ChatMessage message) {
//        messageList.add(message);
//    }
//
//    public void add(List<ChatMessage> messages) {
//        messageList.addAll(messages);
//    }


    private void setAlignment(ViewHolder holder, boolean isMe) {
        if (!isMe) {
            holder.contentWithBG.setBackgroundResource(R.drawable.bubblesilver);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.bubblered1);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);
        }
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        return holder;
    }

    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
    }

}
