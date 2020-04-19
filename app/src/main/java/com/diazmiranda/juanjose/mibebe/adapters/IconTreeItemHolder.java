package com.diazmiranda.juanjose.mibebe.adapters;

import android.content.Context;
import androidx.annotation.ColorRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.diazmiranda.juanjose.mibebe.R;
import com.unnamed.b.atv.model.TreeNode;

public class IconTreeItemHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {

    private TextView tvValue;
    private PrintView arrowView;

    public IconTreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate( R.layout.template_layout_icon_node, null, false);
        tvValue = view.findViewById(R.id.node_value);
        tvValue.setText(value.text);

        final PrintView iconView = view.findViewById(R.id.icon);
        iconView.setIconText(context.getResources().getString(value.icon));

        iconView.setIconColor(value.isVideo ? R.color.youtube : R.color.colorPrimaryDark);

        arrowView = view.findViewById(R.id.arrow_icon);
        arrowView.setVisibility( value.isVideo ? View.GONE : View.VISIBLE );
        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    public static class IconTreeItem {
        public int icon;
        public String text;
        private String videoId;
        private boolean isVideo;
        @ColorRes
        private int color;

        public IconTreeItem(int icon, String text) {
            this.icon = icon;
            this.text = text;
            this.isVideo = false;
        }

        public IconTreeItem(int icon, String text, int color) {
            this.icon = icon;
            this.text = text;
            this.color = color;
            this.isVideo = false;
        }

        public IconTreeItem(int icon, String text, String videoId) {
            this.icon = icon;
            this.text = text;
            this.videoId = videoId;
            this.isVideo = true;
        }

        public IconTreeItem(int icon, String text, String videoId, int color) {
            this.icon = icon;
            this.text = text;
            this.color = color;
            this.videoId = videoId;
            this.isVideo = true;
        }

        public String getVideoId() {
            return videoId;
        }

        public boolean isVideo() {
            return isVideo;
        }

        public int getColor() {
            return color;
        }
    }
}
