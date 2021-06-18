package com.ford.labs.retroquest.thought;

import com.ford.labs.retroquest.columntitle.ColumnTitleRepository;
import com.ford.labs.retroquest.exception.ThoughtNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThoughtService {

    private final ThoughtRepository thoughtRepository;
    private final ColumnTitleRepository columnTitleRepository;

    public ThoughtService(ThoughtRepository thoughtRepository,
                          ColumnTitleRepository columnTitleRepository) {

        this.thoughtRepository = thoughtRepository;
        this.columnTitleRepository = columnTitleRepository;
    }

    public int likeThought(String thoughtId) {
        var thought = fetchThought(thoughtId);
        thought.incrementHearts();
        return thoughtRepository.save(thought).getHearts();
    }

    public void discussThought(String thoughtId) {
        var thought = fetchThought(thoughtId);
        thought.toggleDiscussed();
        thoughtRepository.save(thought);
    }

    public void updateThoughtMessage(String thoughtId, String updatedMessage) {
        var returnedThought = fetchThought(thoughtId);
        returnedThought.setMessage(updatedMessage);
        thoughtRepository.save(returnedThought);
    }

    public Thought fetchThought(String thoughtId) {
        return fetchThought(Long.valueOf(thoughtId));
    }

    public Thought fetchThought(Long thoughtId) {
        return thoughtRepository.findById(thoughtId).orElseThrow(() -> new ThoughtNotFoundException(thoughtId));
    }

    public List<Thought> fetchAllThoughtsByTeam(String teamId) {
        return thoughtRepository.findAllByTeamIdAndBoardIdIsNull(teamId);
    }

    public void deleteAllThoughtsByTeamId(String teamId) {
        thoughtRepository.deleteAllByTeamId(teamId);
    }

    public void deleteThought(String teamId, Long id) {
        thoughtRepository.deleteThoughtByTeamIdAndId(teamId, id);
    }

    public Thought createThought(String teamId, CreateThoughtRequest request) {
        return createThought(teamId, null, request);
    }

    public Thought createThought(String teamId, Long boardId, CreateThoughtRequest request) {
        var thought = new Thought();
        thought.setMessage(request.getMessage());
        thought.setHearts(request.getHearts());
        thought.setTopic(request.getTopic());
        thought.setDiscussed(request.isDiscussed());
        thought.setTeamId(teamId);
        thought.setBoardId(boardId);

        var columnTitle = columnTitleRepository.findByTeamIdAndAndTopic(teamId, thought.getTopic());
        thought.setColumnTitle(columnTitle);
        return thoughtRepository.save(thought);

    }
}
