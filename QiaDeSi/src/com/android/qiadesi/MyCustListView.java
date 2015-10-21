/**
 * 
 */
package com.android.qiadesi;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class MyCustListView extends ListView implements OnScrollListener{

	/**
	 * @param context
	 */
	private OnRefreshListener onRefreshListener;  
    private OnLoadListener onLoadListener; 
    private LayoutInflater inflater;
    private RotateAnimation animation,reverseAnimation;
    private View header;  
    private View footer;
    private TextView loadFull,noData,more,tip,lastUpdate;
    public ProgressBar loading,refreshing;
    private ImageView arrow;
    private int headerContentInitialHeight;
    private int headerContentHeight;
 // 定义header的四种状态和当前状态  
    public static final int NONE = 0;  
    private static final int PULL = 1;  
    private static final int RELEASE = 2;  
    private static final int REFRESHING = 3;  
    public int state = NONE; 
    
    public int firstVisibleItem; 
    private int startY;
    
 // 只有在listview第一个item显示的时候（listview滑到了顶部）才进行下拉刷新， 否则此时的下拉只是滑动listview  
    private boolean isRecorded = false;  
    private boolean isLoading;// 判断是否正在加载  
    private boolean loadEnable = true;// 开启或者关闭加载更多功能  
    private boolean isLoadFull;  
    private int pageSize = 10;
    
    public int scrollState;
    // 区分PULL和RELEASE的距离的大小  
    private static final int SPACE = 20;
    public int currentTotalCount;
	public int visibleItemCount;
	private boolean isLoad = false;
	
	private int footerContentInitialHeight;
	private int footerContentHeight;
	public int footState = NONE; 
	public int totalSize;
    
    public MyCustListView(Context context) {
    	super(context);
		initView(context);
	}
	
	public MyCustListView(Context context, AttributeSet attrs) {  
        super(context, attrs);
        initView(context);
    }  
  
    public MyCustListView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);
        initView(context);  
    }
    
    public void setTotalSize(int totalSize){
    	this.totalSize = totalSize;
    }
    
    public void initView(Context context){
    	// 设置箭头特效  
        animation = new RotateAnimation(0, -180,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);  
        animation.setInterpolator(new LinearInterpolator());  
        animation.setDuration(100);  
        animation.setFillAfter(true);  
  
        reverseAnimation = new RotateAnimation(-180, 0,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);  
        reverseAnimation.setInterpolator(new LinearInterpolator());  
        reverseAnimation.setDuration(500);  
        reverseAnimation.setFillAfter(true);
        
        inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.listview_footer, null);
        loadFull = (TextView) footer.findViewById(R.id.loadFull);  
        noData = (TextView) footer.findViewById(R.id.noData);  
        more = (TextView) footer.findViewById(R.id.more);  
        loading = (ProgressBar) footer.findViewById(R.id.loading);
        
        header = inflater.inflate(R.layout.pull_to_refresh_header, null);  
        arrow = (ImageView) header.findViewById(R.id.arrow);  
        tip = (TextView) header.findViewById(R.id.tip);  
        lastUpdate = (TextView) header.findViewById(R.id.lastUpdate);  
        refreshing = (ProgressBar) header.findViewById(R.id.refreshing); 
        
        arrow.setImageResource(R.drawable.flush);
        
        headerContentInitialHeight = header.getPaddingTop();
        mesureView(header);
        headerContentHeight = header.getMeasuredHeight();
        topPadding(-headerContentHeight);
        
        footerContentInitialHeight = footer.getPaddingBottom();
        mesureView(footer);
        footerContentHeight = footer.getMeasuredHeight();
        bottomPadding(-footerContentHeight);
        
        this.addHeaderView(header);
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }
	
	 /**
	 * @param contentHeight
	 */
	private void topPadding(int paddingTop) {
		header.setPadding(header.getPaddingLeft(), paddingTop, header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate();
	}
	
	private void bottomPadding(int paddingBotton){
		footer.setPadding(footer.getPaddingLeft(), footer.getPaddingTop(), footer.getPaddingRight(), paddingBotton);
		footer.invalidate();
	}

	/**
	 * @param header
	 */
	private void mesureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();  
        if (p == null) {  
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  
                    ViewGroup.LayoutParams.WRAP_CONTENT);  
        }  
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);  
        int lpHeight = p.height;  
        int childHeightSpec;  
        if (lpHeight > 0) {  
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,  
                    MeasureSpec.EXACTLY);  
        } else {  
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,  
                    MeasureSpec.UNSPECIFIED);  
        }  
        child.measure(childWidthSpec, childHeightSpec); 
	}

	// 下拉刷新监听  
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {  
        this.onRefreshListener = onRefreshListener;  
    }  
  
    // 加载更多监听  
    public void setOnLoadListener(OnLoadListener onLoadListener) {  
        this.loadEnable = true;  
        this.onLoadListener = onLoadListener;  
    }  

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		currentTotalCount = totalItemCount;
	}
	
	
	public void onRefresh(){
		if (onRefreshListener != null) {  
            onRefreshListener.onRefresh();  
        }
	}
	
	public void onLoad(){
		if (onLoadListener != null) {  
            onLoadListener.onLoad();  
        }
	}
	
	 /* 
     * 定义下拉刷新接口 
     */
    public interface OnRefreshListener {  
        public void onRefresh();  
    }  
  
    /* 
     * 定义加载更多接口 
     */
    public interface OnLoadListener {  
        public void onLoad();  
    }

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch(ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				if(firstVisibleItem == 0){
					isRecorded = true;
					startY = (int)ev.getY();
				}
				if(firstVisibleItem+visibleItemCount >= currentTotalCount){
					isLoad = true;
					startY = (int)ev.getY();
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				break;
			case MotionEvent.ACTION_UP:
				if(state == PULL){
					state = NONE;
					refreshHeaderByState();
				}else if(state == RELEASE){
					state = REFRESHING;
					refreshHeaderByState();
					onRefresh();
				}
				
				if(footState == PULL){
					footState = NONE;
					refreshFooterByState();
				}else if(footState == RELEASE){
					footState = REFRESHING;
					refreshFooterByState();
					onLoad();
					if(currentTotalCount>=totalSize-1){
						more.setText(R.string.last_load);
					}
				}
				
				isRecorded = false;
				isLoad = false;
				break;
			case MotionEvent.ACTION_MOVE:
				whenMove(ev);
				whenMoveToLast(ev);
				break;
			default:
				break;
		}
		return super.onTouchEvent(ev);
	}

	
	private void whenMoveToLast(MotionEvent ev){
		if(!isLoad){
			return;
		}
		int tmpY = (int)ev.getY();
		int space = startY - tmpY;
		int toPadding = space-footerContentHeight;
		switch(footState){
			case NONE:
				if(space>0){
					footState = PULL;
					refreshFooterByState();
				}
				break;
			case PULL:
				bottomPadding(toPadding);
				if(scrollState == SCROLL_STATE_TOUCH_SCROLL && space > footerContentHeight + SPACE){
					footState = RELEASE;
					refreshFooterByState();
				}
				break;
			case RELEASE:
				bottomPadding(toPadding);
				refreshFooterByState();
				break;
			case REFRESHING:
				topPadding(headerContentInitialHeight);
				loading.setVisibility(View.VISIBLE);
				more.setVisibility(View.GONE);
				break;
			default:
				break;
		}
	}
	
	/**
	 * @param ev
	 */
	private void whenMove(MotionEvent ev) {
		if(!isRecorded){
			return;
		}
		int tmpY = (int) ev.getY();
		int space = tmpY - startY;
		int toPadding = space-headerContentHeight;
		switch(state){
			case NONE:
				if(space>0){
					state = PULL;
					refreshHeaderByState();
				}
				
				break;
			case PULL:
				topPadding(toPadding);
				if(scrollState == SCROLL_STATE_TOUCH_SCROLL && space > headerContentHeight + SPACE){
					state = RELEASE;
					refreshHeaderByState();
				}
				break;
			case RELEASE:
				topPadding(toPadding);
				refreshHeaderByState();
				if(space <= 0){
					state = NONE;
					refreshHeaderByState();
				}
				break;
			case REFRESHING:
				topPadding(headerContentInitialHeight);
				refreshing.setVisibility(View.VISIBLE);
				arrow.clearAnimation();
				arrow.setVisibility(View.GONE);
				tip.setVisibility(View.GONE);
				lastUpdate.setVisibility(View.GONE);
				break;
		}
	}

	/**
	 * 
	 */
	private void refreshHeaderByState() {
		switch(state){
			case NONE:
				topPadding(-headerContentHeight);
				tip.setText(R.string.pull_to_refresh);
				refreshing.setVisibility(View.GONE);
				arrow.clearAnimation();
				
				break;
			case PULL:
				arrow.setVisibility(View.VISIBLE);
				tip.setVisibility(View.VISIBLE);
				lastUpdate.setVisibility(View.VISIBLE);
				refreshing.setVisibility(View.GONE);
				tip.setText(R.string.pull_to_refresh);
				arrow.clearAnimation();
				arrow.setAnimation(reverseAnimation);
				break;
			case RELEASE:
				arrow.setVisibility(View.VISIBLE);
				tip.setVisibility(View.VISIBLE);
				lastUpdate.setVisibility(View.VISIBLE);
				refreshing.setVisibility(View.GONE);
				tip.setText(R.string.release_to_refresh);
				arrow.clearAnimation();
				arrow.setAnimation(animation);
				break;
			case REFRESHING:
				topPadding(headerContentInitialHeight);
				refreshing.setVisibility(View.VISIBLE);
				arrow.clearAnimation();
				arrow.setVisibility(View.GONE);
				tip.setVisibility(View.GONE);
				lastUpdate.setVisibility(View.GONE);
				break;
			default:
				break;
		}
	} 
	
	private void refreshFooterByState() {
		switch(footState){
			case NONE:
				topPadding(-headerContentHeight);
				more.setText(R.string.push_to_load);
				loading.setVisibility(View.GONE);
				
				break;
			case PULL:
				more.setVisibility(View.VISIBLE);
				loading.setVisibility(View.GONE);
				more.setText(R.string.push_to_load);
				break;
			case RELEASE:
				more.setVisibility(View.VISIBLE);
				loading.setVisibility(View.GONE);
				if(currentTotalCount>=totalSize-1){
					more.setText(R.string.last_load);
				}else{
					more.setText(R.string.release_to_load);
				}
				break;
			case REFRESHING:
				bottomPadding(footerContentInitialHeight);
				loading.setVisibility(View.VISIBLE);
				more.setVisibility(View.GONE);
				break;
			default:
				break;
		}
	} 
	
	public void onRefreshComplete(String updateTime) {  
        lastUpdate.setText(this.getContext().getString(R.string.last_update_time,  
                lastUpdate)+updateTime);  
        state = NONE;  
        refreshHeaderByState();  
    }  
  
    // 用于下拉刷新结束后的回调  
    public void onRefreshComplete() {  
    	long time =System.currentTimeMillis();
        String currentTime = getCurrentTime(time);  
        onRefreshComplete(currentTime);  
    }  
  
    // 用于加载更多结束后的回调  
    public void onLoadComplete() {  
        isLoading = false;  
    }  
    public String getCurrentTime(long date) {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String str = format.format(new Date(date));
    	return str;
    	}
}
