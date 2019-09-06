package com.sar.user.fligh_app;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

public class MainActivity extends AppCompatActivity implements RecAdapter.TicketsAdapterListener {
    private static String from = "DEL";
    private static String to = "HYD";
    private Ticket ticket;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static String Tag = MainActivity.class.getSimpleName();
    private RecAdapter recAdapter;
    private ApiService apiService;
    private ArrayList<Ticket> ticketlist = new ArrayList<>();
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.consy)
    ConstraintLayout constraintLayout;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle(from + " > " + to);
        toolbar.setDisplayHomeAsUpEnabled(true);

        apiService = ApiClient.getClient().create(ApiService.class);
        recAdapter = new RecAdapter(this, ticketlist, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recAdapter);
        ConnectableObservable<List<Ticket>> ticketsobservable = getTickets(from, to).replay();
        compositeDisposable.add(ticketsobservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<List<Ticket>>() {

            @Override
            public void onNext(List<Ticket> tickets) {
                ticketlist.clear();
                ticketlist.addAll(tickets);
                recAdapter.notifyDataSetChanged();

            }


            @Override
            public void onError(Throwable e) {
                showError(e);
            }


            @Override
            public void onComplete() {

            }
        }));
        compositeDisposable.add((Disposable) ticketsobservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).flatMap(new Function<List<Ticket>, ObservableSource<Ticket>>() {
            @Override
            public ObservableSource<Ticket> apply(List<Ticket> tickets) throws Exception {
                return Observable.fromIterable(tickets);
            }
        }).flatMap(new Function<Ticket, ObservableSource<Ticket>>() {
            @Override
            public ObservableSource<Ticket> apply(Ticket tickets) throws Exception {
                return getPriceObservablea(ticket);
            }
        }).subscribeWith(new DisposableObserver<Ticket>() {

            @Override
            public void onNext(Ticket ticket) {
                int position = ticketlist.indexOf(ticket);

                if (position == -1) {
                    // TODO - take action
                    // Ticket not found in the list
                    // This shouldn't happen
                    return;
                }

                ticketlist.set(position, ticket);
                recAdapter.notifyItemChanged(position);

            }


            @Override
            public void onError(Throwable e) {
                showError(e);
            }


            @Override
            public void onComplete() {

            }
        }));
        ticketsobservable.connect();
    }

    private Observable<List<Ticket>> getTickets(String from, String to) {
        return apiService.searchTickets(from, to).toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Ticket> getPriceObservablea(final Ticket ticket) {
        return apiService.getPrice(ticket.getFlightNumber(), ticket.getFrom(), ticket.getTo()).toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Function<Price, Ticket>() {

            @Override
            public Ticket apply(Price price) throws Exception {
                ticket.setPrice(price);
                return ticket;
            }
        });


    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onTicketSelected(Ticket contact) {

    }

    /**
     * Snackbar shows observer error
     */
    private void showError(Throwable e) {
        Log.e(TAG, "showError: " + e.getMessage());

        Snackbar snackbar = Snackbar
                .make(constraintLayout, e.getMessage(), Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        unbinder.unbind();
    }
}
