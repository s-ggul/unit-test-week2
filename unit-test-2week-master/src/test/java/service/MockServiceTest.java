package service;

import domain.Champion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.MockRepository;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockServiceTest {

    @Mock
    private MockRepository mockRepository;

    @InjectMocks
    private MockService mockService;

    // ******************************************
    // 기본 mock test method 연습
    // ******************************************

    @Test
    public void 챔피언이름을가져오면_무조건_카이사를_리턴한다() {
        Champion champion = mock(Champion.class);

        //champion.getName()을 호출하면 "카이사"를 리턴한다.
        when(champion.getName()).thenReturn("카이사");
        assertThat(champion.getName(), is("카이사"));
    }

    // 1. when, thenReturn을 사용하여 어떠한 챔피언 이름을 입력해도 베인을 리턴하도록 테스트하세요
    @Test
    public void shouldReturnValueWhenAnythingInput(){
        Champion champion = mock(Champion.class);

        //champion.getName()을 호출하면 "카이사"를 리턴한다.
        when(champion.getName()).thenReturn("베인");
        assertThat(champion.getName(), is("베인"));
    }
    //// 1-1 어떤 포지션을 set 해도 미드를 리턴하도록 테스트하세요.
    @Test
    public void shouldReturnValueWhenAnythingInputInPosition(){
        Champion champion = mock(Champion.class);

        when(champion.getPosition()).thenReturn("미드");
        assertThat(champion.getPosition(),is("미드"));
    }

    // 2. 챔피언 이름으로 야스오를 저장하면, doThrow를 사용하여 Exception이 발생하도록 테스트 하세요.
    @Test(expected = IllegalArgumentException.class)
    public void testChampionNameDoThrow(){
        Champion champion = mock(Champion.class);
        doThrow(new IllegalArgumentException()).when(champion).setName(eq("야스오"));
        champion.setName("야스오");
    }

    // 3. verify 를 사용하여 '미드' 포지션을 저장하는 프로세스가 진행되었는지 테스트 하세요.
    @Test
    public void verifySetPositionMid() {
        Champion champion = mock(Champion.class);
        champion.setPosition("미드");
        verify(champion).setPosition("미드");
    }

    // 4. champion 객체의 크기를 검증하는 로직이 1번 실행되었는지 테스트 하세요.
    @Test
    public void verifyCheckObjectSizeAtOnce() {
        List<Champion> mockChampion = mock(List.class);

        Champion botChamp = mock(Champion.class);
        botChamp.setName("진");
        botChamp.setPosition("원딜");
        botChamp.setHasSkinCount(0);
        mockChampion.add(botChamp);
        /*Champion champion = mock(Champion.class);
        champion.getHasSkinCount();
       */
        System.out.println("size = " + mockChampion.size());
        verify(mockChampion,times(1)).size();
    }

    // 4-1. champion 객체에서 이름을 가져오는 로직이 2번 이상 실행되면 Pass 하는 로직을 작성하세요.
    @Test
    public void shouldAtLeastTwoTimesForChampionGetName(){
        Champion champion = mock(Champion.class);
        champion.setName("조이");
        champion.setPosition("미드");
        champion.setHasSkinCount(3);

        System.out.println(champion.getName());
        System.out.println(champion.getName());
        System.out.println(champion.getName());

        verify(champion,atLeast(3)).getName();
    }
    // 4-2. champion 객체에서 이름을 가져오는 로직이 최소 3번 이하 실행되면 Pass 하는 로직을 작성하세요.
    @Test
    public void shouldAtMostThreeTimesForChampionGetName(){
        Champion champion = mock(Champion.class);
        champion.setName("조이");
        champion.setPosition("미드");
        champion.setHasSkinCount(3);

        System.out.println(champion.getName());
        System.out.println(champion.getName());
        System.out.println(champion.getName());

        verify(champion,atMost(3)).getName();
    }
    // 4-3. champion 객체에서 이름을 저장하는 로직이 실행되지 않았으면 Pass 하는 로직을 작성하세요.
    @Test
    public void shouldPassIfSetNameDoesNotExec(){
        Champion champion = mock(Champion.class);
        champion.setPosition("미드");
        champion.setHasSkinCount(3);

        System.out.println(champion.getName());
        System.out.println(champion.getName());
        System.out.println(champion.getName());

        verify(champion,never()).setName(any(String.class));
    }
    // 4-4. champion 객체에서 이름을 가져오는 로직이 200ms 시간 이내에 1번 실행되었는 지 검증하는 로직을 작성하세요.
    @Test
    public void shouldOneTimeRunForChampionGetName(){
        Champion champion = mock(Champion.class);
        champion.setName("조이");
        champion.setPosition("미드");
        champion.setHasSkinCount(3);

        verify(champion,timeout(100).atLeast(1)).setName(any(String.class));
    }

    // ******************************************
    // injectmock test 연습
    // ******************************************

    @Test
    public void 챔피언정보들을Mocking하고Service메소드호출테스트() {
        when(mockService.findByName(anyString())).thenReturn(new Champion("루시안", "바텀", 5));
        String championName = mockService.findByName("애쉬").getName();
        assertThat(championName, is("루시안"));
        verify(mockRepository, times(1)).findByName(anyString());
    }

    // 1. 리산드라라는 챔피언 이름으로 검색하면 미드라는 포지션과 함께 가짜 객체를 리턴받고, 포지션이 탑이 맞는지를 테스트하세요
    @Test
    public void 가짜객체리턴및포지션체크() {
        when(mockService.findByName("리산드라")).thenReturn(new Champion("리산드라", "미드", 5));
        String championPosition = mockService.findByName("리산드라").getPosition();
        assertThat(championPosition, is("미드"));
    }
    // 2. 2개 이상의 챔피언을 List로 만들어 전체 챔피언을 가져오는 메소드 호출시 그 갯수가 맞는지 확인하는 테스트 코드를 작성하세요.
    @Test
    public void 챔피언리스트() {
        List<Champion> champions = new ArrayList<>();
        champions.add(new Champion("리산드라", "미드", 1));
        champions.add(new Champion("진", "원딜", 0));

        when(mockService.findAllChampions()).thenReturn(champions);
        assertThat(mockService.findAllChampions().size(), is(2));

        verify(mockRepository,times(1)).findAll();
    }
    // 3. 챔피언을 검색하면 가짜 챔피언 객체를 리턴하고, mockRepository의 해당 메소드가 1번 호출되었는지를 검증하고, 그 객체의 스킨 개수가
    //    맞는지 확인하는 테스트코드를 작성하세요.
    @Test
    public void 가짜객체를이용한스킨개수확인(){
        when(mockService.findByName("리산드라")).thenReturn(new Champion("리산드라", "미드", 5));

        System.out.println(mockService.findByName("리산드라").getHasSkinCount());

        assertThat(mockService.findByName("리산드라").getHasSkinCount(), is(5));

        verify(mockRepository,times(2)).findByName(any(String.class));
    }
    // 4. 2개 이상의 가짜 챔피언 객체를 List로 만들어 리턴하고, 하나씩 해당 객체를 검색한 뒤 검색을 위해 호출한 횟수를 검증하세요.
    @Test
    public void 챔피언리스트관련두번재() {
        List<Champion> champions = new ArrayList<>();
        champions.add(new Champion("티모", "탑", 1));
        champions.add(new Champion("진", "원딜", 0));

        when(mockService.findAllChampions()).thenReturn(champions);

        assertThat(mockService.findAllChampions().get(0).getName(), is("티모"));
        assertThat(mockService.findAllChampions().get(1).getName(), is("진"));

        verify(mockRepository,times(2)).findAll();
    }

    //가장 많이 사용되는 테스트 중 하나로 BDD 방식에 기반한 테스트 방법 예제
    @Test
    public void 탐켄치를_호출하면_탐켄치정보를_리턴하고_1번이하로_호출되었는지_검증() {
        //given
        given(mockRepository.findByName("탐켄치")).willReturn(new Champion("탐켄치", "서폿", 4));
        //when
        Champion champion = mockService.findByName("탐켄치");
        //then
        verify(mockRepository, atLeast(1)).findByName(anyString());
        assertThat(champion.getName(), is("탐켄치"));
    }
}