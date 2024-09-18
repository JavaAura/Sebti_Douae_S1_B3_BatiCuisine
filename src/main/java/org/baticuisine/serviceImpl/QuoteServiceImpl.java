package org.baticuisine.serviceImpl;

import org.baticuisine.entities.Quote;
import org.baticuisine.repositoryImpl.QuoteRepositoryImpl;
import org.baticuisine.service.QuoteService;

public class QuoteServiceImpl implements QuoteService {

    private QuoteRepositoryImpl quoteRepository;

    public QuoteServiceImpl(){
        this.quoteRepository = new QuoteRepositoryImpl();
    }

    @Override
    public void addQuote(Quote quote){
        quoteRepository.addQuote(quote);
    }
}
